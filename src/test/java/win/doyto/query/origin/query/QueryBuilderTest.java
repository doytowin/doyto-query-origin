package win.doyto.query.origin.query;

import org.junit.jupiter.api.Test;
import win.doyto.query.origin.module.user.User;
import win.doyto.query.origin.module.user.UserQuery;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * QueryBuilderTest
 *
 * @author f0rb on 2021-10-10
 */
class QueryBuilderTest {

    @Test
    void buildSelectAndArgs() {
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        List<Object> argList = new ArrayList<>();
        String sql = queryBuilder.buildSelectAndArgs(UserQuery.builder().account("admin").valid(true).build(), argList);

        assertEquals("select * from t_user where account = ? and valid = ?", sql);
    }
}