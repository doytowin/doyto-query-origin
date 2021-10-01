package win.doyto.query.origin.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import win.doyto.query.origin.module.user.User;
import win.doyto.query.origin.module.user.UserQuery;
import win.doyto.query.origin.module.user.UserQueryBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * QueryService
 *
 * @author f0rb on 2021-10-01
 */
public class QueryService {
    @Resource
    EntityManager entityManager;

    UserQueryBuilder userQueryBuilder = new UserQueryBuilder(User.class);

    public List<User> query(UserQuery userQuery) {
        List<Object> argList = new ArrayList<>();
        String sql = userQueryBuilder.buildSelectAndArgs(userQuery, argList);
        return executeQuery(sql, argList);
    }

    @SuppressWarnings("unchecked")
    private List<User> executeQuery(String sql, List<Object> argList) {
        Query query = entityManager.createNativeQuery(sql, User.class);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return query.getResultList();
    }

    public Long count(UserQuery userQuery) {
        List<Object> argList = new ArrayList<>();
        String sql = userQueryBuilder.buildCountAndArgs(userQuery, argList);
        return executeCount(sql, argList);
    }

    private Long executeCount(String sql, List<Object> argList) {
        Query query = entityManager.createNativeQuery(sql);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    public Page<User> page(UserQuery userQuery) {
        List<User> list = query(userQuery);
        Long count = count(userQuery);
        return new PageImpl<>(list, userQuery.toPageRequest(), count);
    }
}
