package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.RequestBean;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface RequestService extends BaseService<RequestBean, Integer> {
    void addRequestRecord(String message, String filePath, HttpServletRequest request);

    void addRequestRecord(String message, HttpServletRequest request);

    Optional<RequestBean> findLastByUserId(String requestUserId);
}
