package win.doyto.query.origin.module.role;

import org.springframework.stereotype.Service;
import win.doyto.query.origin.query.QueryService;

/**
 * RoleService
 *
 * @author f0rb on 2021-10-01
 */
@Service
public class RoleService extends QueryService<Role> {
    public RoleService() {
        super(Role.class);
    }
}
