package win.doyto.query.origin.module.user;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import win.doyto.query.origin.DemoApplicationTests;

import java.util.List;
import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * UserServiceTest
 *
 * @author f0rb on 2021-10-01
 */
class UserServiceTest extends DemoApplicationTests {

    @Resource
    UserService userService;

    @Test
    void queryAll() {
        UserQuery query = new UserQuery();
        List<User> users = userService.query(query);
        assertEquals(3, users.size());
    }

    @Test
    void queryByAccount() {
        UserQuery query = UserQuery.builder().account("admin").build();
        List<User> users = userService.query(query);
        assertEquals(1, users.size());
    }

    @Test
    void queryByValid() {
        UserQuery query = UserQuery.builder().valid(true).build();
        List<User> users = userService.query(query);
        assertEquals(2, users.size());
    }

    @Test
    void queryByAccountLike() {
        UserQuery query = UserQuery.builder().accountLike("test").build();
        List<User> users = userService.query(query);
        assertEquals(2, users.size());
    }

    @Test
    void page() {
        UserQuery query = UserQuery.builder().pageNumber(1).pageSize(2).build();
        Page<User> userPage = userService.page(query);
        assertEquals(3, userPage.getTotalElements());
        assertEquals(1, userPage.getContent().size());
    }
}