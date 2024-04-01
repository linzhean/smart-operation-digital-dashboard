package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.GroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.GroupTransformer;

@Service
public class GroupServiceImpl extends BaseServiceImpl<GroupBean, Group, Integer> implements GroupService {
    private final GroupDAO groupDAO;
    private final GroupTransformer transformer;

    public GroupServiceImpl(GroupDAO groupDAO, GroupTransformer transformer) {
        super(groupDAO, transformer);
        this.groupDAO = groupDAO;
        this.transformer = transformer;
    }


    @Override
    public GroupBean save(GroupBean groupBean) {
        return null;
    }
}
