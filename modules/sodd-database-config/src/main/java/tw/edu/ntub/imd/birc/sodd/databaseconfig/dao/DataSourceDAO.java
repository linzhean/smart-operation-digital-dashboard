package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;

import java.util.List;

public interface DataSourceDAO {
    String getJsonData(ChartDataSource dataSource) throws Exception;

    List<Object[]> searchAll(String viewTableName);
}
