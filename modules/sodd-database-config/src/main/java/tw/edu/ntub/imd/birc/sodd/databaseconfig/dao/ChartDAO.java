package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Chart;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChartDAO extends BaseDAO<Chart, Integer> {
    List<Chart> findByAvailableIsTrue();

    List<Chart> findByAvailable(Boolean available);


    Optional<Chart> findByIdAndAvailableIsTrue(Integer id);
}
