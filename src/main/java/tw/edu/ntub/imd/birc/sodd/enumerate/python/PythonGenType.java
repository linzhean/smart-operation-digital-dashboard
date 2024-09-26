package tw.edu.ntub.imd.birc.sodd.enumerate.python;

import lombok.Getter;

@Getter
public enum PythonGenType {
    CHART_PNG("photo"),
    CHART_HTML("html"),
    AI("ai");

    private final String type;

    PythonGenType(String type) {
        this.type = type;
    }

    public static Boolean isChartPhoto(String type) {
        return type.equals(PythonGenType.CHART_PNG.getType());
    }

    public static Boolean isChartHTML(String type) {
        return type.equals(PythonGenType.CHART_HTML.getType());
    }
}
