package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity;

import java.io.Serializable;

public interface Persistable<ID extends Serializable> extends org.springframework.data.domain.Persistable<ID> {
    Boolean getSave();

    void setSave(Boolean isSave);

    @Override
    default boolean isNew() {
        return getSave() != null ? getSave() : getId() == null;
    }
}
