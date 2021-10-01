package win.doyto.query.origin.module.user;


import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import win.doyto.query.origin.query.QueryField;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * UserQueryBuilder
 *
 * @author f0rb on 2021-10-01
 */
public class UserQueryBuilder {
    @SneakyThrows
    public List<String> toWhere(UserQuery query, List<Object> argList) {
        List<String> whereList = new LinkedList<>();
        for (Field field : FieldUtils.getAllFields(query.getClass())) {
            Object value = FieldUtils.readField(field, query, true);
            if (isValidValue(value)) {
                appendAnd(whereList, field);
                appendArg(argList, value);
            }
        }
        return whereList;
    }

    private void appendArg(List<Object> argList, Object value) {
        argList.add(value);
    }

    private void appendAnd(List<String> whereList, Field field) {
        QueryField queryField = field.getAnnotation(QueryField.class);
        whereList.add(queryField.and());
    }

    private boolean isValidValue(Object value) {
        if (value == null) {
            return false;
        } else if (value instanceof CharSequence && StringUtils.isNotBlank((CharSequence) value)) {
            return true;
        } else if (value instanceof Boolean) {
            return true;
        }
        return false;
    }

    public String buildSelectAndArgs(UserQuery query, List<Object> argList) {
        String where = StringUtils.join(toWhere(query, argList), " ");
        if (!where.isEmpty()) {
            where = "where " + where;
        }
        return "select * from t_user " + where;
    }
}