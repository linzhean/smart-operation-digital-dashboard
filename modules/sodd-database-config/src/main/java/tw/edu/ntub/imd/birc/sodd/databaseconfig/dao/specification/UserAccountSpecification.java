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
            Identity.USER,
            Identity.DEVELOPER
    );

    public Specification<UserAccount> checkBlank(
            String departmentId,
            String keyword,
            String identity
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(departmentId)) {
                predicates.add(criteriaBuilder.equal(root.get(UserAccount_.DEPARTMENT_ID), departmentId));
            }
            if (StringUtils.isNotBlank(keyword)) {
                Predicate idPredicate = criteriaBuilder.like(root.get(UserAccount_.USER_ID), "%" + keyword + "%");
                Predicate namePredicate = criteriaBuilder.like(root.get(UserAccount_.USER_NAME), "%" + keyword + "%");
                predicates.add(criteriaBuilder.or(idPredicate, namePredicate));
            }
            if (StringUtils.isNotBlank(identity) && ALLOWED_IDENTITIES.contains(Identity.of(identity))) {
                if (Identity.isNoPermission(Identity.of(identity).getTypeName())) {
                    predicates.add(criteriaBuilder.equal(root.get(UserAccount_.IDENTITY), Identity.NO_PERMISSION));
                } else if (Identity.isUser(Identity.of(identity).getTypeName())) {
                    predicates.add(criteriaBuilder.equal(root.get(UserAccount_.IDENTITY), Identity.USER));
                }
            } else {
                predicates.add(criteriaBuilder.notEqual(root.get(UserAccount_.IDENTITY), Identity.NO_PERMISSION));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}