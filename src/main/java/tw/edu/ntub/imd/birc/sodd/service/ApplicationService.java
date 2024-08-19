package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.ApplicationBean;

import java.util.List;

public interface ApplicationService extends BaseService<ApplicationBean, Integer> {
    List<ApplicationBean> searchApplication(String userId,
                                            String identity,
                                            String status,
                                            String startDate,
                                            String endDate,
                                            Integer nowPage);

    Integer countApplication(String userId, String identity, String status, String startDate, String endDate);

    void permitApplication(ApplicationBean applicationBean, String userId);

    void close(Integer id, ApplicationBean applicationBean);
}
