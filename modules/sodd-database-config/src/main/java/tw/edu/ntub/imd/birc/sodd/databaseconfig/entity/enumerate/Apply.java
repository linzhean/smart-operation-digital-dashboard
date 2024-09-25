package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

@Getter
public enum Apply {
    CLOSED("0", "已關閉"),
    NOT_PASSED("1", "申請尚未通過"),
    PASSED("2", "申請已通過"),
    ACTIVATING("3", "正在啟用");

    private final String value;
    private final String applyStatus;

    Apply(String value, String applyStatus) {
        this.value = value;
        this.applyStatus = applyStatus;
    }

    public static Apply of(String value) {
        for (Apply apply : Apply.values()) {
            if (apply.getValue().equals(value)) {
                return apply;
            }
        }
        return Apply.CLOSED;
    }

    public static String getApplyName(Apply apply) {
        return apply.getApplyStatus();
    }

    public static Boolean isNotPassed(String apply) {
        return apply.equals(Apply.getApplyName(Apply.NOT_PASSED));
    }

    public static Boolean isClosed(String apply) {
        return apply.equals(Apply.getApplyName(Apply.CLOSED));
    }
}
