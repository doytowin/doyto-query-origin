OR语句的三种映射方法
---

## 业务背景

在登录的时候，我们经常会看到账号的输入框里会提示你使用用户名、邮箱或手机号进行登录。对于这种需求，我们可以编写以下SQL语句进行查询：
```sql
SELECT * FROM USER WHERE username = ? OR email = ? OR mobile = ?
```

那么，如何使用字段来映射出这种OR查询语句呢？

## 方法一：使用通用注解`@QueryField`

```java
public class UserQuery {
    @QueryField(and = "username = ? OR email = ? OR mobile = ?")
    private String account;
}
```
从注解中即可得到对应的条件语句
```sql
username = ? OR email = ? OR mobile = ?
```

## 方法二：通过定义的字段名称进行推断

```java
public class UserQuery {
    private String usernameOrEmailOrMobile;
}
```
将字段`usernameOrEmailOrMobile`按关键字`Or`拆开并将首字母小写得到：
```
[username, email, mobile]
```
拼接上` = ?`得到：
```
[username = ?, email = ?, mobile = ?]
```
再用`OR`连接得到查询语句：
```
username = ? OR email = ? OR mobile = ?
```

## 方法三：定义一个实现了`Or`接口的对象

方法一和方法二虽然简单方便，但是只能传入一个同样的参数，无法支持传入不同的参数。这时候我们引入一个接口`Or`，再定义一个`Account`类，实现`Or`接口并定义以下三个字段：

```java
public interface Or {
}

public class Account implements Or {
    private String username;
    private String email;
    private String mobile;
}

public class UserQuery {
    private Account account;
}
```
这样在解析`account`字段的时候，遍历`Account`类定义的字段，可得
```
[username, email, mobile]
```
因`Account`类实现了`Or`接口，于是使用关键字`OR`进行拼接得到查询语句：
```
username = ? OR email = ? OR mobile = ?
```

### 扩展

这种方式可以支持我们定义更加复杂的`Or`语句。例如：

```java
public class Search implements Or {
    private String username;
    private List<Integer> idIn;
}

public class UserQuery {
    private Search search;
    private Boolean isValid;
}
```
对应的查询语句为：
```sql
SELECT * FROM t_user WHERE (username = ? OR id IN (?, ?, ?)) AND isValid = ?
```

## 总结

OR语句的映射也是从通用注解`@QueryField`的使用开始优化。对于入参唯一的简单场景，可以采用字段推导的方法，而对于复杂的OR语句则可以通过定义一个实现了`Or`接口的对象来映射。
