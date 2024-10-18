package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views;

import java.util.List;
import java.util.Map;

public interface CalJsonToInfo {
    Map<String, List<Object>> calJsonToInfo(Map<String, List<Object>> entityListData);
}
