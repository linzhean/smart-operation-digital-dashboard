package tw.edu.ntub.imd.birc.sodd.service.transformer.impl;

import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.JavaBeanUtils;
import tw.edu.ntub.imd.birc.sodd.bean.SyncRecordBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.SyncRecord;
import tw.edu.ntub.imd.birc.sodd.service.transformer.SyncRecordTransformer;

import javax.annotation.Nonnull;

@Component
public class SyncRecordTransformerImpl implements SyncRecordTransformer {
    @Nonnull
    @Override
    public SyncRecord transferToEntity(@Nonnull SyncRecordBean syncRecordBean) {
        return JavaBeanUtils.copy(syncRecordBean, new SyncRecord());
    }

    @Nonnull
    @Override
    public SyncRecordBean transferToBean(@Nonnull SyncRecord syncRecord) {
        return JavaBeanUtils.copy(syncRecord, new SyncRecordBean());
    }
}
