An object-oriented solution for Paging and Sorting in database query
---

## Introduction

Paging and sorting are two of the basic functions provided by databases.

For example, a typical SQL query statement in MySQL is as follows:

```sql
SELECT * FROM t_user
ORDER BY create_time DESC, username ASC
LIMIT 10 OFFSET 20
```

Then, how should the frontend pass the parameters of paging and sorting to the backend? What parameters need to pass? What is the meaning and format of the parameters?

## Paging

The paging statement is `LIMIT 10 OFFSET 20`, where `10` is the size of each page, and `20` is the offset of the query, that is, the 21st to 30th items in the query table, 10 pieces of data totally.

However, when designing the interface, the frontend is usually not required to directly pass in the `OFFSET` parameter. In fact, the value of `OFFSET` is calculated by passing in the page number and page size.

In this case, assuming that the frontend page number starts from 1, the data to be queried is the data on the third page with a page size of 10, and the corresponding `OFFSET` value is calculated as `(3-1)*10 = 20`.

When the frontend calls the backend query interface, it usually passes in the page number and the size of each page, and the backend calculates `OFFSET` to do paging query. Here, the parameter name of the page number is defined as `pageNumber`, and the parameter name of the page size is defined as `pageSize`, and assuming that the page number starts from 1, then the calculation formula of `OFFSET` would be:
```
(pageNumber - 1) * pageSize
```

Then the frontend should pass the parameters like this:
````
?pageNumber=3&pageSize=10
````

## Paging Dialect

The paging statements for different databases are not consistent, but they generally require three parameters: SQL statement, page size and offset.

So we can define an interface [`Dialect`](https://github.com/doytowin/doyto-query/blob/main/doyto-query-api/src/main/java/win/doyto/query/core/Dialect.java) for building paging statements, and provide the corresponding paging implementation according to the database used.
```java
public interface Dialect {
    String buildPageSql(String sql, int limit, long offset);
}
```

## Sorting

The sorting statement is `ORDER BY create_time DESC, username ASC`, where `ORDER BY` is a SQL keyword, which can be defined as a parameter name. I named it `sort` since it is used for sorting, and the corresponding value is `create_time DESC, username ASC`. The GET request needs to escape the space in the parameter value. In order to avoid this problem, we can convert the `,` in the value to `;`, and convert the space before `DESC`/`ASC` to `,`. The final parameter sent by frontend will look like this:
```
?sort=create_time,desc;username,asc
```

## Request Object

Combining the above parameter definitions and descriptions for paging and sorting, the frontend values can send parameters as follows:
```
?pageNumber=3&pageSize=10&sort=create_time,desc;username,asc
```
Then we can define a class [`PageQuery`](https://github.com/doytowin/doyto-query/blob/main/doyto-query-api/src/main/java/win/doyto/query/core/PageQuery.java) to process paging and sorting parameters:
```java
public class PageQuery {
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
}
```

Each query has explicit or implicit paging and sorting, such as:
```sql
SELECT * FROM t_user
```
is EQUAL to
```sql
SELECT * FROM t_user
ORDER BY id ASC
LIMIT ∞ OFFSET 0
```

So `PageQuery` should be used as the parent class of all query objects in order to provide paging and sorting capabilities for data queries.

## Response Object

For the frontend paging query request, in addition to returning the corresponding data list, it also needs to return the total number of data `total` to help the frontend calculate the total number of pages. The calculation formula is `⌈total/size⌉`. The corresponding response object is defined as follows:

```java
public class PageList<T> {
    private final List<T> list;
    private final long total;
}
```

## Conclusion

This article mainly introduces the object-oriented solution for paging and sorting in database queries, that's all.

