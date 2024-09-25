package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.SyncRecordBean;

import java.time.LocalDateTime;

public interface SyncRecordService extends BaseService<SyncRecordBean, Integer>{
    LocalDateTime getLastSyncTime();
}
