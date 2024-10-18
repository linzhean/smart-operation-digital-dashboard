package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupDAO extends BaseDAO<Group, Integer> {
    List<Group> findByAvailableIsTrue();


    Optional<Group> findByIdAndAvailableIsTrue(Integer id);
}
