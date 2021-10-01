package win.doyto.query.origin.module.user;

import lombok.*;

import java.io.Serializable;

/**
 * UserQuery
 *
 * @author f0rb on 2021-10-01
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserQuery implements Serializable {
    private String account;
    private Boolean valid;
    private String accountLike;
}
