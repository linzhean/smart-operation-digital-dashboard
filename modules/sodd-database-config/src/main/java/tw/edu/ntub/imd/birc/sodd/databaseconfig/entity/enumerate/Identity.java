package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

public enum Identity {
    NO_PERMISSION("0", "無權限"),
    MANAGER("1", "高階主管"),
    EMPLOYEE("2", "員工"),
    ADMIN("3", "管理員");

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

    public static boolean isNoPermission(String identity) {
        return identity.equals(Identity.NO_PERMISSION.getTypeName());
    }

    public static boolean isManager(String identity) {
        return identity.equals(Identity.MANAGER.getTypeName());
    }

    public static boolean isEmployee(String identity) {
        return identity.equals(Identity.EMPLOYEE.getTypeName());
    }
}
