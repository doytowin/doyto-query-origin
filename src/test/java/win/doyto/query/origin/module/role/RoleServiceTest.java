package win.doyto.query.origin.module.role;

import org.junit.jupiter.api.Test;
import win.doyto.query.origin.DemoApplicationTests;

import java.util.List;
import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * RoleServiceTest
 *
 * @author f0rb on 2021-10-01
 */
class RoleServiceTest extends DemoApplicationTests {

    @Resource
    RoleService roleService;

    @Test
    void query() {
        List<Role> roles = roleService.query(new RoleQuery());
        assertEquals(0, roles.size());
    }
}