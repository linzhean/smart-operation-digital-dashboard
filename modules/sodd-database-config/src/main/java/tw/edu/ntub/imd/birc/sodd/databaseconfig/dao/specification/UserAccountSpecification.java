package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.UserAccount_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@Component
public class UserAccountSpecification {
    private static final EnumSet<Identity> ALLOWED_IDENTITIES = EnumSet.of(
            Identity.NO_PERMISSION,
            Identity.MANAGER,
            Identity.DEVELOPER
    );

    public Specification<UserAccount> checkBlank(
            String departmentId,
            String name,
            String identity
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(departmentId)) {
                predicates.add(criteriaBuilder.equal(root.get(UserAccount_.DEPARTMENT_ID), departmentId));
            }
            if (StringUtils.isNotBlank(name)) {
                predicates.add(criteriaBuilder.like(root.get(UserAccount_.USER_NAME), "%" + name + "%"));
            }
            if (StringUtils.isNotBlank(identity) && ALLOWED_IDENTITIES.contains(Identity.of(identity))) {
                if (Identity.isNoPermission(Identity.of(identity).getTypeName())) {
                    predicates.add(criteriaBuilder.equal(root.get(UserAccount_.IDENTITY), Identity.NO_PERMISSION));
                } else if (Identity.isManager(Identity.of(identity).getTypeName())) {
                    predicates.add(criteriaBuilder.equal(root.get(UserAccount_.IDENTITY), Identity.MANAGER));
                }
            } else {
                predicates.add(criteriaBuilder.notEqual(root.get(UserAccount_.IDENTITY), Identity.NO_PERMISSION));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}