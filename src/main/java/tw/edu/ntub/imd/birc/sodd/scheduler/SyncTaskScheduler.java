package tw.edu.ntub.imd.birc.sodd.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tw.edu.ntub.imd.birc.sodd.bean.AssignedTaskBean;
import tw.edu.ntub.imd.birc.sodd.bean.ChartBean;
import tw.edu.ntub.imd.birc.sodd.bean.SyncRecordBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.ChartDataSource;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.views.CalJsonToInfo;
import tw.edu.ntub.imd.birc.sodd.exception.NotFoundException;
import tw.edu.ntub.imd.birc.sodd.scheduler.factory.DataSyncUtilFactory;
import tw.edu.ntub.imd.birc.sodd.service.AssignedTaskService;
import tw.edu.ntub.imd.birc.sodd.service.ChartService;
import tw.edu.ntub.imd.birc.sodd.service.SyncRecordService;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SyncTaskScheduler {
    @Autowired
    private DataSyncUtilFactory dataSyncUtilFactory;
    private final SyncRecordService syncRecordService;
    private final AssignedTaskService assignedTaskService;
    private final ChartService chartService;


    public SyncTaskScheduler(SyncRecordService syncRecordService,
                             AssignedTaskService assignedTaskService,
                             ChartService chartService) {
        this.syncRecordService = syncRecordService;
        this.assignedTaskService = assignedTaskService;
        this.chartService = chartService;
    }


    @Scheduled(fixedRate = 600000)
    public void checkTableUpdates() throws Exception {
        syncMSSQLToMySQL();
        assignedTaskService.checkChartDataIndicators();
    }

    private void syncMSSQLToMySQL() {
        List<Class<?>> classes = null;
        try {
            classes = getClasses("tw/edu/ntub/imd/birc/sodd/databaseconfig/entity/erp");
        } catch (Exception e) {
            throw new RuntimeException("取得/entity/erp內的類別失敗");
        }
        for (Class<?> c : classes) {
            dataSync(c);
        }
        SyncRecordBean syncRecordBean = new SyncRecordBean();
        syncRecordBean.setSyncTime(LocalDateTime.now());
        syncRecordService.save(syncRecordBean);
    }

    public List<Class<?>> getClasses(String packageName) throws Exception {
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
