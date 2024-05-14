package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationSpecification {
    public Specification<Application> checkBlank(
            String userId,
            String identity,
            String status,
            String startDate,
            String endDate
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Identity.isAdmin(identity)) {
                predicates.add(criteriaBuilder.equal(root.get(Application_.CREATE_ID), userId));
            }
            if (StringUtils.isNotBlank(status)) {
                predicates.add(criteriaBuilder.equal(root.get(Application_.APPLY_STATUS), status));
            }
            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = LocalDateTime.parse(startDate);
                LocalDateTime endDateTime = LocalDateTime.parse(endDate);
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(Application_.CREATE_DATE), startDateTime));
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(Application_.END_DATE), endDateTime));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
