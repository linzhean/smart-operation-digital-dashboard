package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

public enum RequestType {
    GET("0", "GET"),
    POST("1", "POST"),
    PATCH("2", "PATCH"),
    DELETE("3", "DELETE");

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

    public static RequestType getByTypeName(String typeName) {
        for (RequestType authority : RequestType.values()) {
            if (authority.getTypeName().equals(typeName)) {
                return authority;
            }
        }
        return RequestType.GET;
    }

    public static String getRequestTypeName(RequestType authority) {
        return "[" + authority.getTypeName() + "]";
    }
}
