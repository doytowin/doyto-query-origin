SpringDataJPA之从入门到弃用
---


本文主要讲弃用。

_入门_

入门的文章一大堆，基本上学会使用`JpaRepository`编写增删查改代码，使用`Specification`编写动态查询就可以。

比如编写这样一个[`UserSpecification`](https://github.com/doytowin/doyto-query-origin/blob/step0/src/main/java/win/doyto/query/origin/module/user/UserSpecification.java)类，

```java
public class UserSpecification implements Specification<User> {

    private final UserQuery query;

    public UserSpecification(UserQuery query) {
        this.query = query;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Predicate> list = new ArrayList<>();

        if (StringUtils.isNotBlank(query.getAccount())) {
            list.add(criteriaBuilder.equal(root.get("account").as(String.class), query.getAccount()));
        }
        if (StringUtils.isNotBlank(query.getUserStatus())) {
            list.add(criteriaBuilder.equal(root.get("status").as(String.class), query.getStatus()));
        }
        if (StringUtils.isNotBlank(query.getAccountLike())) {
            list.add(criteriaBuilder.like(root.get("account").as(String.class), "%" + query.getAccountLike() + "%"));
        }
        return criteriaBuilder.and(list.toArray(new Predicate[0]));
    }
}
```

然后我们来演示如何通过对这个`UserSpecification`类进行重构，来替换掉复杂难用的`SpringDataJPA`的。

_重构_

在分析代码之前，先介绍点重构的知识。

“重构就是在不改变软件系统外部行为的前提下，对它的内部结构进行改善”。

这句话大家可能都听过,但是重构的两大目标许多人却未必清楚。

其中一个是表达意图，另一个是消除重复。

_表达意图_

那么`UserSpecification`类背后要表达的意图是什么呢？

单看这段代码只是将`UserQuery`类定义的对象`query`转换成`Predicate`对象，再结合所要完成的动态查询功能来看，这个`Predicate`对象最终将被转换成SQL语句用于数据库查询。

那么我们为什么不绕过`Predicate`对象，直接把`query`对象转换成SQL语句，来达到我们进行动态查询的意图呢？

于是，我们编写一个`UserQueryBuilder`类来基于`UserQuery`构建查询语句和参数，

```java
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
```

并在`UserService`中将生成的查询语句和参数交给`EntityManager`执行。
```java
@Service
public class UserService {
    @Resource
    EntityManager entityManager;

    UserQueryBuilder userQueryBuilder = new UserQueryBuilder();

    public List<User> query(UserQuery userQuery) {
        ArrayList<Object> argList = new ArrayList<>();
        String sql = userQueryBuilder.buildSelectAndArgs(userQuery, argList);

        Query query = entityManager.createNativeQuery(sql, User.class);
        for (int i = 0; i < argList.size(); i++) {
            query.setParameter(i + 1, argList.get(i));
        }
        return query.getResultList();
    }
}
```

> 完整代码见这里：https://github.com/doytowin/doyto-query-origin/tree/step1

这时我们已经不再需要使用`UserSpecification`类来进行动态查询了。

_消除重复_

代码重复分为两种，一种是简单型重复，一种是结构型重复。

再看看`UserQueryBuilder#toWhere`方法，每个if语句的代码都比较相似
```
if (some condition) {
    whereList.add(sql);
    argList.add(value);
}
```
这就是一种典型的结构型重复，而消除这种重复的常见手法就是使用反射。

另外每行`whereList.add`语句里面的参数都是字符串常量，并且跟`UserQuery`里的字段一一对应，可以通过引入注解来将查询语句和字段关联起来。

经过这一步重构后的代码结构大概是这样:
- 通过反射获取查询对象的所有字段并进行遍历
    - 通过反射读取对应字段的值
    - 如果为有效值
        - 向whereList添加注解配置的查询条件
        - 向argList添加字段的值

重构后的`UserQueryBuilder`代码如下：
```java
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
    // ...
}
```

对应的`UserQuery`代码如下：
```java
public class UserQuery extends PageQuery {

    @QueryField(and = "account = ?")
    private String account;

    @QueryField(and = "valid = ?")
    private Boolean valid;

    @QueryField(and = "account Like CONCAT('%', ?, '%')")
    private String accountLike;

}
```

以及`QueryField`的定义：
```java
@Target(FIELD)
@Retention(RUNTIME)
public @interface QueryField {
    String and();
}
```

> 完整代码见这里：https://github.com/doytowin/doyto-query-origin/tree/step2

现在当我们需要增加新的查询条件时，只需要在`UserQuery`类中添加新的字段并配上相应的注解即可。

_弃用_

接着再通过对`UserQueryBuilder`和`UserService`应用抽取父类，抽取泛型参数等重构手法，
将动态查询逻辑进一步封装和抽象，进而得到一个将Query对象映射成Where语句的独立模块。

后续再基于Entity类生成增删查改语句，交给`EntityManager`执行，从而不再需要使用`JpaRepository`，
最后将SQL执行模块从`EntityManager`切换为`spring-jdbc`，彻底完成对`SpringDataJPA`的替换。

这就是第二代ORM框架`DoytoQuery`的起源。
