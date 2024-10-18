package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;

import java.util.List;

public interface UserAccountService extends BaseService<UserAccountBean, String> {
    List<UserAccountBean> searchByUserValue(String departmentId, String keyword, String identity, Integer nowPage);

    List<UserAccountBean> searchByUserValue(String departmentId, String keyword, String identity);

    Integer countUserList(String departmentId, String identity, String keyword);
}
