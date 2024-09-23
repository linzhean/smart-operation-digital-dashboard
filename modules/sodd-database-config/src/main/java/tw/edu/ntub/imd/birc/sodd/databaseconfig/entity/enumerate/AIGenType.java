package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

@Getter
public enum AIGenType {
    AI("0", "assistant"),
    USER("1", "user");

    private final String value;
    private final String type;

    AIGenType(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public static AIGenType of(String value) {
        for (AIGenType aiGenType : AIGenType.values()) {
            if (aiGenType.getValue().equals(value)) {
                return aiGenType;
            }
        }
        return AIGenType.USER;
    }
}
