package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao;

import org.springframework.stereotype.Repository;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Department;

import java.util.List;

@Repository
public interface DepartmentDAO extends BaseDAO<Department, Integer> {
    List<Department> findByAvailableIsTrue();
}
