package win.doyto.query.origin.module.user;

import org.springframework.stereotype.Service;

import java.util.List;
import javax.annotation.Resource;

/**
 * UserService
 *
 * @author f0rb on 2021-10-01
 */
@Service
public class UserService {

    @Resource
    UserRepository userRepository;

    public List<User> query(UserQuery userQuery) {
        return userRepository.findAll(new UserSpecification(userQuery));
    }

}
