package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserGroup_;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserGroupSpecification {
    public Specification<UserGroup> checkBlank(
            Integer groupId,
            String departmentId,
            String position
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<UserAccount, UserGroup> userGroupJoin = root.join(UserGroup_.USER_ACCOUNT);
            predicates.add(criteriaBuilder.equal(root.get(UserGroup_.GROUP_ID), groupId));
            predicates.add(criteriaBuilder.equal(root.get(UserGroup_.AVAILABLE), true));
            if (StringUtils.isNotBlank(departmentId)) {
                predicates.add(criteriaBuilder.equal(userGroupJoin.get(UserAccount_.DEPARTMENT_ID), departmentId));
            }
            if (StringUtils.isNotBlank(position)) {
                predicates.add(criteriaBuilder.equal(userGroupJoin.get(UserAccount_.POSITION), position));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
