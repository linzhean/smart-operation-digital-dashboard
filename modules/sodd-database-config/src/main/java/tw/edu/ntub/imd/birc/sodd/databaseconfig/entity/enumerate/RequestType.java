package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

public enum RequestType {
    GET("0", "get"),
    POST("1", "post"),
    PATCH("2", "patch"),
    DELETE("3", "delete");

    @Getter
    private final String value;
    @Getter
    private final String typeName;

    RequestType(String value, String typeName) {
        this.value = value;
        this.typeName = typeName;
    }

    public static RequestType of(String value) {
        for (RequestType authority : RequestType.values()) {
            if (authority.getValue().equals(value)) {
                return authority;
            }
        }
        return RequestType.GET;
    }

    public static String getRequestTypeName(RequestType authority) {
        return "[" + authority.getTypeName() + "]";
    }
}
