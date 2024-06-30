package tw.edu.ntub.imd.birc.sodd.databaseconfig.dao.specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import tw.edu.ntub.birc.common.util.StringUtils;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Application_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.Mail_;
import tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.enumerate.Identity;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MailSpecification {
    public Specification<Mail> checkBlank(
            String userId,
            String status
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(userId)) {
                predicates.add(criteriaBuilder.equal(root.get(Mail_.RECEIVER), userId));
            }
            if (StringUtils.isNotBlank(status)) {
                predicates.add(criteriaBuilder.equal(root.get(Mail_.STATUS), status));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

    }
}
