package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate;

import lombok.Getter;

public enum ProcessStatus {
    PENDING("2", "待處理"),
    SUCCEEDED("3", "已完成");

    @Getter
    private final String value;
    @Getter
    private final String processStatus;

    ProcessStatus(String value, String processStatus) {
        this.value = value;
        this.processStatus = processStatus;
    }

    public static ProcessStatus of(String value) {
        for (ProcessStatus processStatus : ProcessStatus.values()) {
            if (processStatus.getValue().equals(value)) {
                return processStatus;
            }
        }
        return ProcessStatus.PENDING;
    }

    public static String getProcessStatusName(ProcessStatus processStatus) {
        return processStatus.getProcessStatus();
    }

    public static Boolean isPending(ProcessStatus processStatus) {
        return processStatus.equals(ProcessStatus.PENDING);
    }

    public static Boolean isSucceeded(ProcessStatus processStatus) {
        return processStatus.equals(ProcessStatus.SUCCEEDED);
    }
}
