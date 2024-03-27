package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseDAO<E, ID extends Serializable> extends BaseViewDAO<E, ID>, JpaRepository<E, ID> {
    @Nonnull
    @Override
    Optional<E> findById(@Nonnull ID id);

    @Override
    boolean existsById(@Nonnull ID id);

    default <S extends E> S update(S entity) {
        return save(entity);
    }

    default <S extends E> List<S> updateAll(Iterable<S> entities) {
        return saveAll(entities);
    }

    default <S extends E> S updateAndFlush(S entity) {
        return saveAndFlush(entity);
    }
}
