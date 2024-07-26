package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Request;

import java.util.List;

@Repository
public interface RequestDAO extends BaseDAO<Request, Integer> {
    List<Request> findByRequestUserId(String requestUserId, Sort sort);
}
