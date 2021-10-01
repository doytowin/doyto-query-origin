package win.doyto.query.origin.module.user;


import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * UserQueryBuilder
 *
 * @author f0rb on 2021-10-01
 */
public class UserQueryBuilder {
    public List<String> toWhere(UserQuery query, List<Object> argList) {
        List<String> whereList = new LinkedList<>();

        if (StringUtils.isNotBlank(query.getAccount())) {
            whereList.add("account = ?");
            argList.add(query.getAccount());
        }
        if (query.getValid() != null) {
            whereList.add("valid = ?");
            argList.add(query.getValid());
        }
        if (StringUtils.isNotBlank(query.getAccountLike())) {
            whereList.add("account like ?");
            argList.add("%" + query.getAccountLike() + "%");
        }
        return whereList;
    }

    public String buildSelectAndArgs(UserQuery query, List<Object> argList) {
        String where = StringUtils.join(toWhere(query, argList), " ");
        if (!where.isEmpty()) {
            where = "where " + where;
        }
        return "select * from t_user " + where;
    }
}