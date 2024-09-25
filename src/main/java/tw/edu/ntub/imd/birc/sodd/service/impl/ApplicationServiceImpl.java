package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.*;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ApplicationDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.ApplicationSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.service.*;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ApplicationTransformer;
import tw.edu.ntub.imd.birc.sodd.util.email.EmailUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ApplicationServiceImpl extends BaseServiceImpl<ApplicationBean, Application, Integer> implements ApplicationService {
    private final Integer SIZE = 10;

    private final ApplicationDAO applicationDAO;
    private final ApplicationTransformer transformer;
    private final ApplicationSpecification specification;
    private final UserGroupService userGroupService;
    private final GroupService groupService;
    private final ChartService chartService;
    private final ChartGroupService chartGroupService;
    private final UserAccountService userAccountService;
    private final EmailUtils emailUtils;
    private final TaskScheduler taskScheduler;
    private ScheduledFuture startTask;
    private ScheduledFuture endTask;

    public ApplicationServiceImpl(ApplicationDAO applicationDAO,
                                  ApplicationTransformer transformer,
                                  ApplicationSpecification specification,
                                  UserGroupService userGroupService,
                                  GroupService groupService,
                                  ChartService chartService,
                                  ChartGroupService chartGroupService,
                                  UserAccountService userAccountService,
                                  EmailUtils emailUtils,
                                  TaskScheduler taskScheduler) {
        super(applicationDAO, transformer);
        this.applicationDAO = applicationDAO;
        this.transformer = transformer;
        this.specification = specification;
        this.userGroupService = userGroupService;
        this.groupService = groupService;
        this.chartService = chartService;
        this.chartGroupService = chartGroupService;
        this.userAccountService = userAccountService;
        this.emailUtils = emailUtils;
        this.taskScheduler = taskScheduler;
    }

    @Override
    public ApplicationBean save(ApplicationBean applicationBean) {
        Application application = transformer.transferToEntity(applicationBean);
        application.setApplicant(SecurityUtils.getLoginUserAccount());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        application.setStartDate(LocalDateTime.parse(applicationBean.getStartDateStr(), formatter));
        application.setEndDate(LocalDateTime.parse(applicationBean.getEndDateStr(), formatter));
        return transformer.transferToBean(applicationDAO.save(application));
    }

    @Override
    public List<ApplicationBean> searchApplication(String userId,
                                                   String identity,
                                                   String status,
                                                   String startDate,
                                                   String endDate,
                                                   Integer nowPage) {
        Page<Application> applicationPage = applicationDAO.findAll(specification.checkBlank(
                        userId, identity, status, startDate, endDate),
                PageRequest.of(nowPage, SIZE, Sort.by(
                        Sort.Order.desc(Application_.MODIFY_DATE),
                        Sort.Order.desc(Application_.CREATE_DATE),
                        Sort.Order.desc(Application_.START_DATE))));
        return CollectionUtils.map(applicationPage.getContent(), transformer::transferToBean);
    }

    @Override
    public Integer countApplication(String userId,
                                    String identity,
                                    String status,
                                    String startDate,
                                    String endDate) {
        return applicationDAO.findAll(specification.checkBlank(
                                userId, identity, status, startDate, endDate),
                        PageRequest.of(0, SIZE, Sort.by(
                                Sort.Order.desc(Application_.MODIFY_DATE),
                                Sort.Order.desc(Application_.CREATE_DATE),
                                Sort.Order.desc(Application_.START_DATE))))
                .getTotalPages();
    }

    @Override
    public void permitApplication(ApplicationBean applicationBean, String userId) {
        Application application = transformer.transferToEntity(applicationBean);
        String email = userAccountService.getById(userId)
                .map(UserAccountBean::getGmail)
                .orElseThrow(() -> new NotFoundException("查無此使用者"));
        changeApplyStatus(application, Apply.PASSED);
        emailUtils.sendMail(applicationBean.getApplicant(), email, "申請審核通過通知",
                "src/main/resources/mail/applicationPermitted.html", null);
        Instant startInstant = application.getStartDate().atZone(ZoneId.systemDefault()).toInstant();
        Instant endInstant = application.getEndDate().atZone(ZoneId.systemDefault()).toInstant();
        // 申請啟用任務裡啟動結束任務
        startTask = taskScheduler.schedule(() -> {
            changeApplyStatus(application, Apply.ACTIVATING);
            int groupId = getGroupId(application);
            int userGroupId = saveUserGroup(userId, groupId);
            endTask = taskScheduler.schedule(() -> {
                changeApplyStatus(application, Apply.CLOSED);
                removeUserGroup(userGroupId, userId, groupId);
            }, endInstant);
        }, startInstant);
    }

    private int getGroupId(Application application) {
        ChartBean chartBean = chartService.getById(application.getChartId())
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        GroupBean groupBean = new GroupBean();
        groupBean.setName(application.getApplicant() + " "
                + chartBean.getName() + " "
                + application.getStartDate() + "/"
                + application.getEndDate());    // 申請臨時群組格式 申請人 圖表名 開始日/結束日
        groupBean = groupService.save(groupBean);
        ChartGroupBean chartGroupBean = new ChartGroupBean();
        chartGroupBean.setChartId(chartBean.getId());
        chartGroupBean.setGroupId(groupBean.getId());
        chartGroupService.save(chartGroupBean);
        return groupBean.getId();
    }

    private void changeApplyStatus(Application application, Apply apply) {
        application.setApplyStatus(apply);
        applicationDAO.update(application);
    }

    private int saveUserGroup(String userId, Integer groupId) {
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setUserId(userId);
        userGroupBean.setGroupId(groupId);
        userGroupBean = userGroupService.save(userGroupBean);
        return userGroupBean.getId();
    }

    private void removeUserGroup(Integer userGroupId, String userId, Integer groupId) {
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setAvailable(false);
        userGroupBean.setModifyId(userId);
        userGroupService.update(userGroupId, userGroupBean);
        groupService.delete(groupId);
    }

    @Override
    public void close(Integer id, ApplicationBean applicationBean) {
        changeApplyStatus(transformer.transferToEntity(applicationBean), Apply.CLOSED);
        startTask.cancel(true);
        endTask.cancel(true);
    }
}
