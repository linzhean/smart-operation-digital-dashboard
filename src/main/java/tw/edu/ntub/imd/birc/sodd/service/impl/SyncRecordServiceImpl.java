package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.SyncRecordBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.SyncRecordDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.SyncRecord;
import tw.edu.ntub.imd.birc.sodd.service.SyncRecordService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.SyncRecordTransformer;

import java.time.LocalDateTime;

@Service
public class SyncRecordServiceImpl extends BaseServiceImpl<SyncRecordBean, SyncRecord, Integer> implements SyncRecordService {
    private final SyncRecordDAO syncRecordDAO;
    private final SyncRecordTransformer transformer;

    public SyncRecordServiceImpl(SyncRecordDAO syncRecordDAO, SyncRecordTransformer transformer) {
        super(syncRecordDAO, transformer);
        this.syncRecordDAO = syncRecordDAO;
        this.transformer = transformer;
    }

    @Override
    public SyncRecordBean save(SyncRecordBean syncRecordBean) {
        SyncRecord syncRecord = transformer.transferToEntity(syncRecordBean);
        return transformer.transferToBean(syncRecordDAO.save(syncRecord));
    }

    @Override
    public LocalDateTime getLastSyncTime() {
        return syncRecordDAO.findByOrderBySyncTimeDesc()
                .stream()
                .findFirst()
                .map(SyncRecord::getSyncTime)
                .orElseThrow(() -> new RuntimeException("資料尚未開始同步"));
    }
}
