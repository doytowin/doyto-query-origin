package win.doyto.query.origin.module.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * UserService
 *
 * @author f0rb on 2021-10-01
 */
@Service
@SuppressWarnings("unchecked")
public class UserService {

    @Resource
    UserRepository userRepository;

    @Resource
    EntityManager entityManager;

    UserQueryBuilder userQueryBuilder = new UserQueryBuilder();

    public List<User> query(UserQuery userQuery) {
        ArrayList<Object> argList = new ArrayList<>();
        String sql = userQueryBuilder.buildSelectAndArgs(userQuery, argList);

        Query query = entityManager.createNativeQuery(sql, User.class);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return query.getResultList();
    }

    public Page<User> page(UserQuery userQuery) {
        UserSpecification userSpecification = new UserSpecification(userQuery);
        PageRequest pageRequest = userQuery.toPageRequest();
        return userRepository.findAll(userSpecification, pageRequest);
    }

}
