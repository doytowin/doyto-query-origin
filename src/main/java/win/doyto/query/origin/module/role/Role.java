package win.doyto.query.origin.module.role;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Role
 *
 * @author f0rb on 2021-10-01
 */
@Entity
@Table(name = "t_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends AbstractPersistable<Integer> {
    private String roleName;
}