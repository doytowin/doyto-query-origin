package win.doyto.query.origin.module.user;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;

/**
 * UserSpecification
 *
 * @author f0rb on 2021-10-01
 */
public class UserSpecification implements Specification<User> {

    private final UserQuery query;

    public UserSpecification(UserQuery query) {
        this.query = query;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();

        if (StringUtils.isNotBlank(query.getAccount())) {
            list.add(criteriaBuilder.equal(root.get("account").as(String.class), query.getAccount()));
        }
        if (query.getValid() != null) {
            list.add(criteriaBuilder.equal(root.get("valid").as(Boolean.class), query.getValid()));
        }
        if (StringUtils.isNotBlank(query.getAccountLike())) {
            list.add(criteriaBuilder.like(root.get("account").as(String.class), "%" + query.getAccountLike() + "%"));
        }
        return criteriaBuilder.and(list.toArray(new Predicate[0]));
    }
}