package win.doyto.query.origin.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * QueryService
 *
 * @author f0rb on 2021-10-01
 */
public class QueryService<E> {
    @Resource
    EntityManager entityManager;

    QueryBuilder queryBuilder;
    private Class<E> clazz;

    public QueryService(Class<E> clazz) {
        this.clazz = clazz;
        queryBuilder = new QueryBuilder(this.clazz);
    }

    public List<E> query(PageQuery pageQuery) {
        List<Object> argList = new ArrayList<>();
        String sql = queryBuilder.buildSelectAndArgs(pageQuery, argList);
        return executeQuery(sql, argList);
    }

    @SuppressWarnings("unchecked")
    private List<E> executeQuery(String sql, List<Object> argList) {
        Query query = entityManager.createNativeQuery(sql, this.clazz);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return query.getResultList();
    }

    public Long count(PageQuery pageQuery) {
        List<Object> argList = new ArrayList<>();
        String sql = queryBuilder.buildCountAndArgs(pageQuery, argList);
        return executeCount(sql, argList);
    }

    private Long executeCount(String sql, List<Object> argList) {
        Query query = entityManager.createNativeQuery(sql);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return ((BigInteger) query.getSingleResult()).longValue();
    }

    public Page<E> page(PageQuery pageQuery) {
        List<E> list = query(pageQuery);
        Long count = count(pageQuery);
        return new PageImpl<>(list, pageQuery.toPageRequest(), count);
    }
}
