package win.doyto.query.origin.module.user;

import org.springframework.stereotype.Service;
import win.doyto.query.origin.query.QueryService;

import javax.annotation.Resource;

/**
 * UserService
 *
 * @author f0rb on 2021-10-01
 */
@Service
public class UserService extends QueryService<User> {

    @Resource
    UserRepository userRepository;

    public UserService() {
        super(User.class);
    }
}
