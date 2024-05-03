package tw.edu.ntub.imd.birc.sodd.service;

import tw.edu.ntub.imd.birc.sodd.bean.DepartmentBean;

import java.util.Map;


public interface DepartmentService extends BaseService<DepartmentBean, Integer> {
    Map<String, String> getDepartmentMap();
}
