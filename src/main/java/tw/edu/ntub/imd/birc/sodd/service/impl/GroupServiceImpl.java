package tw.edu.ntub.imd.birc.sodd.service.impl;

import org.springframework.stereotype.Service;
import tw.edu.ntub.birc.common.util.CollectionUtils;
import tw.edu.ntub.imd.birc.sodd.bean.GroupBean;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.GroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.UserGroupDAO;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Group;
import tw.edu.ntub.imd.birc.sodd.service.GroupService;
import tw.edu.ntub.imd.birc.sodd.service.transformer.GroupTransformer;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl extends BaseServiceImpl<GroupBean, Group, Integer> implements GroupService {
    private final GroupDAO groupDAO;
    private final UserGroupDAO userGroupDAO;
    private final GroupTransformer transformer;

    public GroupServiceImpl(GroupDAO groupDAO, UserGroupDAO userGroupDAO, GroupTransformer transformer) {
        super(groupDAO, transformer);
        this.groupDAO = groupDAO;
        this.userGroupDAO = userGroupDAO;
        this.transformer = transformer;
    }


    @Override
    public GroupBean save(GroupBean groupBean) {
        Group group = transformer.transferToEntity(groupBean);
        return transformer.transferToBean(groupDAO.save(group));
    }

    @Override
    public List<GroupBean> searchByUserId(String userId) {
        List<Group> groups = userGroupDAO.findByUserIdAndAvailableIsTrue(userId)
                .stream()
                .map(userGroup -> groupDAO.findById(userGroup.getGroupId()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return CollectionUtils.map(groups, transformer::transferToBean);
    }

    @Override
    public List<GroupBean> searchAll() {
        return CollectionUtils.map(groupDAO.findByAvailableIsTrue(), transformer::transferToBean);
    }
}
