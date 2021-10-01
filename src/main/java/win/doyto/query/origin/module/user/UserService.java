package win.doyto.query.origin.module.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import win.doyto.query.origin.query.QueryService;

import javax.annotation.Resource;

/**
 * UserService
 *
 * @author f0rb on 2021-10-01
 */
@Service
public class UserService extends QueryService {

    @Resource
    UserRepository userRepository;

    public Page<User> page(UserQuery userQuery) {
        UserSpecification userSpecification = new UserSpecification(userQuery);
        PageRequest pageRequest = userQuery.toPageRequest();
        return userRepository.findAll(userSpecification, pageRequest);
    }

}
