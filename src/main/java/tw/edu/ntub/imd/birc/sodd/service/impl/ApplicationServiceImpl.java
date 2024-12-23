package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.beans.factory.annotation.Value;
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

import java.time.*;
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

    @Value("${sodd.mail.template.applicationPermitted}")
    private String mailPath;

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
                mailPath, null);
        Instant startInstant = application.getStartDate().atZone(ZoneId.of("Asia/Taipei")).toInstant();
        Instant endInstant = application.getEndDate().atZone(ZoneId.of("Asia/Taipei")).toInstant();
        // 申請啟用任務裡啟動結束任務
        startTask = taskScheduler.schedule(() -> {
            changeApplyStatus(application, Apply.ACTIVATING);
            int groupId = saveGroup(application, userId);
            endTask = taskScheduler.schedule(() -> {
                changeApplyStatus(application, Apply.CLOSED);
                groupService.delGroup(groupId);
                groupService.checkNotAccessibleChart(userId);
            }, endInstant);
        }, startInstant);
    }

    private int saveGroup(Application application, String userId) {
        ChartBean chartBean = chartService.getById(application.getChartId())
                .orElseThrow(() -> new NotFoundException("查無此圖表"));
        GroupBean groupBean = new GroupBean();
        groupBean.setName("申請 - " + application.getApplicant());
        groupBean = groupService.save(groupBean);
        ChartGroupBean chartGroupBean = new ChartGroupBean();
        chartGroupBean.setChartId(chartBean.getId());
        chartGroupBean.setGroupId(groupBean.getId());
        chartGroupService.save(chartGroupBean);
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setUserId(userId);
        userGroupBean.setGroupId(groupBean.getId());
        userGroupService.save(userGroupBean);
        return groupBean.getId();
    }

    private void changeApplyStatus(Application application, Apply apply) {
        application.setApplyStatus(apply);
        applicationDAO.update(application);
    }


    @Override
    public void close(Integer id, ApplicationBean applicationBean) {
        changeApplyStatus(transformer.transferToEntity(applicationBean), Apply.CLOSED);
        if (startTask != null && !startTask.isDone()) {
            startTask.cancel(true); // 取消尚未執行的startTask
        }

        if (endTask != null && !endTask.isDone()) {
            endTask.cancel(true); // 取消尚未執行的endTask
        }
    }
}
