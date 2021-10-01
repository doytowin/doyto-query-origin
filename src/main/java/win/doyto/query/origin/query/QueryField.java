package win.doyto.query.origin.query;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * QueryField
 *
 * @author f0rb on 2021-10-01
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface QueryField {
    String and();
}

