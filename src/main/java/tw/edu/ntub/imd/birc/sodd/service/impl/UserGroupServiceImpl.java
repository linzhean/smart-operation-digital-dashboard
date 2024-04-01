package tw.edu.ntub.imd.birc.sodd.service.impl;

import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.service.UserGroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserGroupTransformer;

public class UserGroupServiceImpl extends BaseServiceImpl<UserGroupBean, UserGroup, Integer> implements UserGroupService {
    private final UserGroupDAO userGroupDAO;
    private final UserGroupTransformer transformer;


    public UserGroupServiceImpl(UserGroupDAO userGroupDAO, UserGroupTransformer transformer) {
        super(userGroupDAO, transformer);
        this.userGroupDAO = userGroupDAO;
        this.transformer = transformer;
    }

    @Override
    public UserGroupBean save(UserGroupBean userGroupBean) {
        return null;
    }
}
