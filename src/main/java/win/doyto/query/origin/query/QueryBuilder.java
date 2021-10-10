package win.doyto.query.origin.query;

import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Table;

/**
 * QueryBuilder
 *
 * @author f0rb on 2021-10-01
 */
public class QueryBuilder {

    protected QueryBuilder(Class<?> clazz) {
        this.tableName = clazz.getDeclaredAnnotation(Table.class).name();
    }

    private String tableName;

    @SneakyThrows
    public List<String> toWhere(PageQuery query, List<Object> argList) {
        List<String> whereList = new LinkedList<>();
        for (Field field : getAllFields(query)) {
            Object value = readValue(query, field);
            if (isValidValue(value)) {
                appendAnd(whereList, field);
                appendArg(argList, value);
            }
        }
        return whereList;
    }

    private Field[] getAllFields(PageQuery query) {
        return FieldUtils.getAllFields(query.getClass());
    }

    private Object readValue(PageQuery query, Field field) throws IllegalAccessException {
        return FieldUtils.readField(field, query, true);
    }

    private void appendArg(List<Object> argList, Object value) {
        argList.add(value);
    }

    private void appendAnd(List<String> whereList, Field field) {
        QueryField queryField = field.getAnnotation(QueryField.class);
        if (queryField != null) {
            whereList.add(queryField.and());
        } else {
            whereList.add(field.getName() + " = ?");
        }
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

    public String buildSelectAndArgs(PageQuery query, List<Object> argList) {
        String where = StringUtils.join(toWhere(query, argList), " and ");
        if (!where.isEmpty()) {
            where = " where " + where;
        }
        String page = "";
        if (query.needPaging()) {
            page = " LIMIT " + query.getPageNumber() + " OFFSET " + query.getPageNumber()* query.getPageSize();
        }
        return "select * from " + tableName + where + page;
    }

    public String buildCountAndArgs(PageQuery query, List<Object> argList) {
        String where = StringUtils.join(toWhere(query, argList), " ");
        if (!where.isEmpty()) {
            where = " where " + where;
        }
        return "select count(*) from " + tableName + where;
    }

}
