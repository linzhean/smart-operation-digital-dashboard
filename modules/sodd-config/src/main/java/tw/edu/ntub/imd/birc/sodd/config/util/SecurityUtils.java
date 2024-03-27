package tw.edu.ntub.imd.birc.sodd.config.util;

import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    public static final Integer REFRESH_HOUR = 168;
    public static final String HAS_ADMIN_AUTHORITY = "hasAuthority('管理者')";

    private SecurityUtils() {

    }

    public static String getLoginUserAccount() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static String getLoginUserIdentity() {
        return String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
    }

    public static boolean isLogin() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }


}
