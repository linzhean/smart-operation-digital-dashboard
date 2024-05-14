package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

public enum Identity {
    NO_PERMISSION("0", "無權限"),
    USER("1", "一般使用者"),
    ADMIN("2", "管理員");

    @Getter
    private final String value;
    @Getter
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
}
