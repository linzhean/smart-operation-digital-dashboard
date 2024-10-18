package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

@Getter
public enum Identity {
    NO_PERMISSION("0", "無權限"),
    USER("1", "一般使用者"),
    DEVELOPER("2", "開發者"),
    ADMIN("3", "管理員");

    private final String value;
    private final String typeName;

    Identity(String value, String typeName) {
        this.value = value;
        this.typeName = typeName;
    }

    public static Identity of(String value) {
        for (Identity identity : Identity.values()) {
            if (identity.getValue().equals(value)) {
                return identity;
            }
        }
        return Identity.NO_PERMISSION;
    }

    public static String getIdentityName(Identity identity) {
        return "[" + identity.getTypeName() + "]";
    }

    public static boolean isAdmin(String identity) {
        return identity.equals(Identity.getIdentityName(Identity.ADMIN));
    }

    public static boolean isAdminTypeName(String identity) {
        return identity.equals(Identity.ADMIN.getTypeName());
    }

    public static boolean isNoPermission(String identity) {
        return identity.equals(Identity.NO_PERMISSION.getTypeName());
    }

    public static boolean isUser(String identity) {
        return identity.equals(Identity.USER.getTypeName());
    }

    public static boolean isDeveloper(String identity) {
        return identity.equals(Identity.DEVELOPER.getTypeName());
    }
}
