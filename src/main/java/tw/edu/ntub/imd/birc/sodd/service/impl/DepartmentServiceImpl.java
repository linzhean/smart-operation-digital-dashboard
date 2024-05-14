package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.DepartmentBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.DepartmentDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Department;
import tw.edu.ntub.imd.birc.sodd.service.DepartmentService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.DepartmentTransformer;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentBean, Department, Integer> implements DepartmentService {
    private final DepartmentDAO departmentDAO;
    private final DepartmentTransformer transformer;

    public DepartmentServiceImpl(DepartmentDAO departmentDAO, DepartmentTransformer transformer) {
        super(departmentDAO, transformer);
        this.departmentDAO = departmentDAO;
        this.transformer = transformer;
    }


    @Override
    public DepartmentBean save(DepartmentBean departmentBean) {
        return null;
    }

    @Override
    public Map<String, String> getDepartmentMap() {
        return departmentDAO.findByAvailableIsTrue()
                .stream()
                .collect(Collectors.toMap(Department::getId, Department::getName));
    }
}
