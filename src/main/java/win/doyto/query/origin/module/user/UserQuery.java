package win.doyto.query.origin.module.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import win.doyto.query.origin.query.PageQuery;
import win.doyto.query.origin.query.QueryField;

/**
 * UserQuery
 *
 * @author f0rb on 2021-10-01
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery extends PageQuery {

    @QueryField(and = "account = ?")
    private String account;

    @QueryField(and = "nickname = ?")
    private String nickname;

    @QueryField(and = "email = ?")
    private String email;

    @QueryField(and = "account Like CONCAT('%', ?, '%')")
    private String accountLike;

    @QueryField(and = "nickname LIKE CONCAT('%', ?, '%')")
    private String nicknameLike;

    @QueryField(and = "email LIKE CONCAT('%', ?, '%')")
    private String emailLike;

    @QueryField(and = "valid = ?")
    private Boolean valid;
}
