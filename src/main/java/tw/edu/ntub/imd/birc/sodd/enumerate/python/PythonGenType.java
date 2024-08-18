package tw.edu.ntub.imd.birc.sodd.enumerate.python;

import lombok.Getter;

@Getter
public enum PythonGenType {
    CHART_PNG("png"),
    CHART_HTML("html"),
    AI("ai");

    private final String type;

    PythonGenType(String type) {
        this.type = type;
    }
}
