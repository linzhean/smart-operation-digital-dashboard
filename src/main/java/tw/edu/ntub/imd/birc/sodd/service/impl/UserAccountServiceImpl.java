package tw.edu.ntub.imd.birc.sodd.service.impl;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.GroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.service.UserAccountService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserAccountTransformer;
import tw.edu.ntub.imd.birc.sodd.util.EmailTransformUtils;

import java.util.*;

@Service
public class UserAccountServiceImpl extends BaseServiceImpl<UserAccountBean, UserAccount, String> implements UserAccountService, UserDetailsService {
    private final UserAccountDAO userAccountDAO;
    private final UserGroupDAO userGroupDAO;
    private final GroupDAO groupDAO;
    private final UserAccountTransformer transformer;
    @Value("${google.clientId}")
    private String clientId;

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO, UserGroupDAO userGroupDAO, GroupDAO groupDAO, UserAccountTransformer transformer) {
        super(userAccountDAO, transformer);
        this.userAccountDAO = userAccountDAO;
        this.userGroupDAO = userGroupDAO;
        this.groupDAO = groupDAO;
        this.transformer = transformer;
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
                email = EmailTransformUtils.remove(email);
                Optional<UserAccount> optional = userAccountDAO.findById(email);

                UserAccount userAccount;
                if (optional.isEmpty()) {
                    userAccount = new UserAccount();
                    userAccount.setUserId(email);
                    userAccount.setUserName((String) payload.get("name"));
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
                        userGroupDAO.findByUserIdAndAvailableIsTrue(username)
                                .stream()
                                .map(userGroup -> groupDAO.getById(userGroup.getGroupId()).getName())
                                .forEach(groupName -> authorities.add(new SimpleGrantedAuthority(groupName)));
                        return authorities;
                    }

                    @Override
                    public String getPassword() {
                        return googleId;
                    }

                    @Override
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
}
