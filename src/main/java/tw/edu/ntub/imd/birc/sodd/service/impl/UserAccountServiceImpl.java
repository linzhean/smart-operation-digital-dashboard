package tw.edu.ntub.imd.birc.sodd.service.impl;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.GroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.UserAccountSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount_;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserAccountTransformer;
import tw.edu.ntub.imd.birc.sodd.util.EmailTransformUtils;

import java.util.*;

@Service
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccountBean, UserAccount, String>
        implements UserAccountService, UserDetailsService {
    private static final int SIZE = 10;
    private final UserAccountDAO userAccountDAO;
    private final UserGroupDAO userGroupDAO;
    private final GroupDAO groupDAO;
    private final UserAccountTransformer transformer;
    private final UserAccountSpecification specification;
    @Value("${google.clientId}")
    private String clientId;

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO,
                                  UserGroupDAO userGroupDAO,
                                  GroupDAO groupDAO,
                                  UserAccountTransformer transformer,
                                  UserAccountSpecification specification) {
        super(userAccountDAO, transformer);
        this.userAccountDAO = userAccountDAO;
        this.userGroupDAO = userGroupDAO;
        this.groupDAO = groupDAO;
        this.transformer = transformer;
        this.specification = specification;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        GoogleIdTokenVerifier verifier =
                new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                        .setAudience(Collections.singletonList(clientId))
                        .build();
        try {
            GoogleIdToken idToken = verifier.verify(username);
            if (idToken != null) {
                IdToken.Payload payload = idToken.getPayload();
                String googleId = payload.getSubject();

                String email = (String) payload.get("email");
                String userId = EmailTransformUtils.remove(email);
                Optional<UserAccount> optional = userAccountDAO.findById(userId);

                UserAccount userAccount;
                if (optional.isEmpty()) {
                    userAccount = new UserAccount();
                    userAccount.setUserId(userId);
                    userAccount.setUserName((String) payload.get("name"));
                    userAccount.setGmail(email);
                    userAccount.setGoogleId(googleId);
                    userAccount.setAvailable(true);

                    userAccountDAO.save(userAccount);
                } else {
                    userAccount = optional.get();
                }

                return new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        List<GrantedAuthority> authorities = new ArrayList<>();
                        authorities.add(new SimpleGrantedAuthority(userAccount.getIdentity().getTypeName()));
                        return authorities;
                    }

                    @Override
                    public String getPassword() {
                        return googleId;
                    }

                    public String getUsername() {
                        return userAccount.getUserId();
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return userAccount.getAvailable();
                    }
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new UsernameNotFoundException("登入失敗");
    }

    @Override
    public UserAccountBean save(UserAccountBean userAccountBean) {
        return null;
    }

    @Override
    public Integer countUserList(String departmentId, String identity, String name) {
        return userAccountDAO.findAll(
                        specification.checkBlank(departmentId, identity, name),
                        PageRequest.of(0, SIZE))
                .getTotalPages();
    }

    @Override
    public List<UserAccountBean> searchByUserValue(String departmentId, String name, String identity, Integer nowPage) {
        Page<UserAccount> userAccountPage = userAccountDAO.findAll(
                specification.checkBlank(departmentId, name, identity),
                PageRequest.of(nowPage, SIZE, Sort.by(
                        Sort.Order.asc(UserAccount_.IDENTITY),
                        Sort.Order.asc(UserAccount_.DEPARTMENT_ID)
                )));
        return CollectionUtils.map(userAccountPage.getContent(), transformer::transferToBean);
    }

    @Override
    public List<UserAccountBean> searchByUserValue(String departmentId, String name, String identity) {
        List<UserAccount> userAccountList = userAccountDAO.findAll(
                specification.checkBlank(departmentId, name, identity));
        return CollectionUtils.map(userAccountList, transformer::transferToBean);
    }
}
