package win.doyto.query.origin.query;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;

import java.io.Serializable;

/**
 * PageQuery
 *
 * @author f0rb on 2021-10-01
 */
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class PageQuery implements Serializable {
    private Integer pageNumber;
    private Integer pageSize;

    public PageRequest toPageRequest() {
        return PageRequest.of(getPageNumber(), getPageSize());
    }

    public int getPageNumber() {
        if (pageNumber == null || pageNumber < 0) {
            return 0;
        }
        return pageNumber;
    }

    public int getPageSize() {
        if (pageSize == null || pageSize < 0) {
            return 10;
        }
        return pageSize;
    }

    public boolean needPaging() {
        return pageNumber != null || pageSize != null;
    }

}
