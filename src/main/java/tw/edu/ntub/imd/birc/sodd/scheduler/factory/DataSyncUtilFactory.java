package tw.edu.ntub.imd.birc.sodd.scheduler.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.scheduler.DataSyncUtils;

@Component
public class DataSyncUtilFactory {
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    @Qualifier("mssqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    // 工廠方法
    public <T> DataSyncUtils<T> createDataSyncUtils(Class<T> clazz) {
        DataSyncUtils<T> dataSyncUtils = new DataSyncUtils<>(clazz);

        // 動態獲取 SyncDAO
        dataSyncUtils.setApplicationContext(applicationContext);
        dataSyncUtils.setJdbcTemplate(jdbcTemplate);

        return dataSyncUtils;
    }
}
