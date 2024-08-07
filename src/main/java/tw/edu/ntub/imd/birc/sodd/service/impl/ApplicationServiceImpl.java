package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.ApplicationBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.ApplicationDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.ApplicationSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Apply;
import tw.edu.ntub.imd.birc.sodd.service.ApplicationService;
import tw.edu.ntub.imd.birc.sodd.service.UserGroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.ApplicationTransformer;

import java.time.Instant;
import java.time.LocalDateTime;
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
    private final TaskScheduler taskScheduler;
    private ScheduledFuture startTask;
    private ScheduledFuture endTask;

    public ApplicationServiceImpl(ApplicationDAO applicationDAO,
                                  ApplicationTransformer transformer,
                                  ApplicationSpecification specification,
                                  UserGroupService userGroupService,
                                  TaskScheduler taskScheduler) {
        super(applicationDAO, transformer);
        this.applicationDAO = applicationDAO;
        this.transformer = transformer;
        this.specification = specification;
        this.userGroupService = userGroupService;
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
    public Integer countApplication(String userId, String identity, String status, String startDate, String endDate) {
        return applicationDAO.findAll(specification.checkBlank(
                                userId, identity, status, startDate, endDate),
                        PageRequest.of(0, SIZE, Sort.by(
                                Sort.Order.desc(Application_.MODIFY_DATE),
                                Sort.Order.desc(Application_.CREATE_DATE),
                                Sort.Order.desc(Application_.START_DATE))))
                .getTotalPages();
    }

    @Override
    public void permitApplication(ApplicationBean applicationBean, String userId, Integer groupId) {
        Application application = transformer.transferToEntity(applicationBean);
        application.setApplyStatus(Apply.PASSED);
        applicationDAO.update(application);
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setUserId(userId);
        userGroupBean.setGroupId(groupId);
        startTask = taskScheduler.schedule(() -> startViewingChart(application, userGroupBean),
                Instant.from(application.getStartDate()));
        endTask = taskScheduler.schedule(() -> userGroupService.removeUserFromGroup(userId, groupId),
                Instant.from(application.getEndDate()));
    }

    private void startViewingChart(Application application, UserGroupBean userGroupBean) {
        application.setApplyStatus(Apply.ACTIVATING);
        applicationDAO.update(application);
        userGroupService.save(userGroupBean);
    }

    @Override
    public void close(Integer id, ApplicationBean applicationBean) {
        applicationBean.setApplyStatus(Apply.CLOSED);
        update(id, applicationBean);
        startTask.cancel(true);
        endTask.cancel(true);
    }
}
