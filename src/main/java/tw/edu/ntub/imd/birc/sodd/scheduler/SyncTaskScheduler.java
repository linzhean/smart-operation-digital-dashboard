package tw.edu.ntub.imd.birc.sodd.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.bean.SyncRecordBean;
import tw.edu.ntub.imd.birc.sodd.scheduler.factory.DataSyncUtilFactory;
import tw.edu.ntub.imd.birc.sodd.service.SyncRecordService;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SyncTaskScheduler {
    private final DataSource dataSource;
    @Autowired
    private DataSyncUtilFactory dataSyncUtilFactory;
    private final SyncRecordService syncRecordService;


    public SyncTaskScheduler(DataSource dataSource, SyncRecordService syncRecordService) {
        this.dataSource = dataSource;
        this.syncRecordService = syncRecordService;
    }


    @Scheduled(fixedRate = 600000)
    public void checkTableUpdates() throws Exception {
        List<Class<?>> classes = getClasses("tw/edu/ntub/imd/birc/sodd/databaseconfig/entity/erp");
        for (Class<?> c : classes) {
            dataSync(c);
        }
        SyncRecordBean syncRecordBean = new SyncRecordBean();
        syncRecordBean.setSyncTime(LocalDateTime.now());
        syncRecordService.save(syncRecordBean);
    }

    public static List<Class<?>> getClasses(String packageName) throws Exception {
        String path = packageName.replace('/', '.');
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(packageName);
        File directory = new File(resource.getFile());

        List<Class<?>> classes = new ArrayList<>();
        if (directory.exists()) {
            for (String file : directory.list()) {
                if (file.endsWith(".class") && !file.contains("_") &&
                        file.substring(0, file.lastIndexOf(".")).length() == 5) {
                    classes.add(Class.forName(path + '.' + file.substring(0, file.length() - 6)));
                }
            }
        }
        return classes;
    }

    private <T> void dataSync(Class<T> tClass) {
        DataSyncUtils<T> dataSyncUtils = dataSyncUtilFactory.createDataSyncUtils(tClass);
        dataSyncUtils.syncAllData();
    }
}
