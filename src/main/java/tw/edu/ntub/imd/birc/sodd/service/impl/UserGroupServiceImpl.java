package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.UserAccountBean;
import tw.edu.ntub.imd.birc.sodd.bean.UserGroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserAccountDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification.UserGroupSpecification;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroupId;
import tw.edu.ntub.imd.birc.sodd.service.UserGroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserAccountTransformer;
import tw.edu.ntub.imd.birc.sodd.service.transformer.UserGroupTransformer;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl extends BaseServiceImpl<UserGroupBean, UserGroup, UserGroupId> implements UserGroupService {
    private final UserGroupDAO userGroupDAO;
    private final UserAccountDAO userAccountDAO;
    private final UserGroupTransformer transformer;
    private final UserAccountTransformer userAccountTransformer;
    private final UserGroupSpecification userGroupSpecification;

    public UserGroupServiceImpl(UserGroupDAO userGroupDAO,
                                UserAccountDAO userAccountDAO,
                                UserGroupTransformer transformer,
                                UserAccountTransformer userAccountTransformer,
                                UserGroupSpecification userGroupSpecification) {
        super(userGroupDAO, transformer);
        this.userGroupDAO = userGroupDAO;
        this.userAccountDAO = userAccountDAO;
        this.transformer = transformer;
        this.userAccountTransformer = userAccountTransformer;
        this.userGroupSpecification = userGroupSpecification;
    }

    @Override
    public UserGroupBean save(UserGroupBean userGroupBean) {
        UserGroup userGroup = transformer.transferToEntity(userGroupBean);
        return transformer.transferToBean(userGroupDAO.save(userGroup));
    }

    @Override
    public void removeUserFromGroup(String userId, Integer groupId) {
        UserGroupBean userGroupBean = new UserGroupBean();
        userGroupBean.setAvailable(false);
        UserGroupId userGroupId = new UserGroupId(userId, groupId);
        super.update(userGroupId, userGroupBean);
    }

    @Override
    public List<UserAccountBean> searchUserByGroupId(Integer groupId, String userName, String department, String position) {
        List<UserAccount> userAccounts = userGroupDAO.findAll(
                        userGroupSpecification.checkBlank(groupId, department, position))
                .stream()
                .map(userGroup -> userAccountDAO.getById(userGroup.getUserId()))
                .collect(Collectors.toList());
        return CollectionUtils.map(userAccounts, userAccountTransformer::transferToBean);
    }
}
