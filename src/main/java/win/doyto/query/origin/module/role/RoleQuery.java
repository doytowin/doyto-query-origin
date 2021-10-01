package win.doyto.query.origin.module.role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import win.doyto.query.origin.query.PageQuery;

/**
 * RoleQuery
 *
 * @author f0rb on 2021-10-01
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class RoleQuery extends PageQuery {

    private String roleName;

}
