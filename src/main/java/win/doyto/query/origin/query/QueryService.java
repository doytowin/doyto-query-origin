package win.doyto.query.origin.query;

import win.doyto.query.origin.module.user.User;
import win.doyto.query.origin.module.user.UserQuery;
import win.doyto.query.origin.module.user.UserQueryBuilder;

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
}
