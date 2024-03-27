package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseViewDAO<E, ID extends Serializable> extends Repository<E, ID>, QueryByExampleExecutor<E> {
    @Nonnull
    Optional<E> findById(@Nonnull ID id);

    @Nonnull
    List<E> findAll();

    @Nonnull
    List<E> findAllById(@Nonnull Iterable<ID> ids);

    @Nonnull
    List<E> findAll(@Nonnull Sort sort);

    /**
     * @param pageable 分頁、排序
     * @return 查詢到的清單
     * @see org.springframework.data.domain.PageRequest
     */
    @Nonnull
    Page<E> findAll(@Nonnull Pageable pageable);

    @Override
    @Nonnull
    <S extends E> List<S> findAll(@Nonnull Example<S> example);

    @Override
    @Nonnull
    <S extends E> List<S> findAll(@Nonnull Example<S> example, @Nonnull Sort sort);

    @Override
    @Nonnull
    <S extends E> Page<S> findAll(@Nonnull Example<S> example, @Nonnull Pageable pageable);

    boolean existsById(ID id);

    long count();
}
