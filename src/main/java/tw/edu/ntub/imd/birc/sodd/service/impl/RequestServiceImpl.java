package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.bean.RequestBean;
import tw.edu.ntub.imd.birc.sodd.config.util.SecurityUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.RequestDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Request;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Request_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.RequestType;
import tw.edu.ntub.imd.birc.sodd.service.RequestService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.RequestTransformer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class RequestServiceImpl extends BaseServiceImpl<RequestBean, Request, Integer> implements RequestService {
    private final RequestDAO requestDAO;
    private final UserAccountDAO userAccountDAO;
    private final RequestTransformer transformer;

    public RequestServiceImpl(RequestDAO requestDAO, UserAccountDAO userAccountDAO, RequestTransformer transformer) {
        super(requestDAO, transformer);
        this.requestDAO = requestDAO;
        this.userAccountDAO = userAccountDAO;
        this.transformer = transformer;
    }

    @Override
    public RequestBean save(RequestBean requestBean) {
        Request request = transformer.transferToEntity(requestBean);
        return transformer.transferToBean(requestDAO.save(request));
    }

    @Override
    public void addRequestRecord(String message, String filePath, HttpServletRequest request) {
        RequestBean bean = addRecord(message, request);
        bean.setFilePath(filePath);
        requestDAO.save(transformer.transferToEntity(bean));
    }

    private RequestBean addRecord(String message, HttpServletRequest request) {
        RequestBean requestBean = new RequestBean();
        String userId = SecurityUtils.getLoginUserAccount();
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotEquals(userId, "anonymousUser")) {
            requestBean.setRequestUserId(userId);
            Optional<UserAccount> userAccountOptional = userAccountDAO.findById(userId);
            if (userAccountOptional.isPresent()) {
                requestBean.setIdentity(Identity.of(userId));
            } else {
                requestBean.setIdentity(Identity.NO_PERMISSION);
            }
        } else {
            requestBean.setIdentity(Identity.NO_PERMISSION);
        }
        requestBean.setMapping(RequestType.valueOf(request.getMethod()));
        requestBean.setUserAgent(request.getHeader("user-agent"));
        requestBean.setUrl(request.getRequestURI());
        requestBean.setStatus(request.getMethod());
        requestBean.setRequestIpFrom(request.getRemoteAddr());
        return requestBean;
    }

    @Override
    public void addRequestRecord(String message, HttpServletRequest request) {
        requestDAO.save(transformer.transferToEntity(addRecord(message, request)));
    }

    @Override
    public Optional<RequestBean> findLastByUserId(String requestUserId) {
        return requestDAO.findByRequestUserId(requestUserId,
                        Sort.by(Sort.Order.desc(Request_.REQUEST_TIME)))
                .stream()
                .findFirst()
                .map(transformer::transferToBean);
    }
}
