package win.doyto.query.origin.module.user;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * User
 *
 * @author f0rb on 2021-10-01
 */
@Entity
@Table(name = "t_user")
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends AbstractPersistable<Integer> {

    private String account;

    private String email;

    private String mobile;

    private Boolean valid;

}
