package org.h0110w.som.core.service.util.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * used for an interaction with database - allows to request to present list of objects that are stored in db
 * converts list of raw filter strings into Specification
 * convert raw sort string into Sort
 * @see PageRequest
 * @see Specification
 * @see Sort
 */
@Getter
@Setter
public class CustomPageRequest {
    /**
     * number of page to display
     */
    private Integer page;
    /**
     * amount of elements that should be displayed on one page
     */
    private Integer size;
    /**
     * list of filters
     * pattern - property:operation:value
     * example - login:like:user1
     */
    private List<String> filter;
    /**
     * string that contains data about field by which elements must be sorted
     * pattern - property:direction
     * example - login:asc
     */
    private String sort;

    public Pageable getPageable() {
        return PageRequest.of(page, size, getProcessedSort());
    }

    private Sort getProcessedSort() {
        if (StringUtils.isEmpty(this.sort)) {
            return Sort.unsorted();
        }
        String[] items = sort.split(":", 2);
        if (items.length != 2) {
            throw new IllegalArgumentException("invalid sort parameter");
        }
        Arrays.stream(items).forEach(item -> {
            if (StringUtils.isEmpty(item)) {
                throw new IllegalArgumentException("invalid sort parameter");
            }
        });
        Sort.Direction direction = Sort.Direction.fromString(items[1]);
        return Sort.by(direction, items[0]);
    }

    @JsonIgnore
    public <T> Specification<T> getSpecification() {
        Specification<T> spec = Specification.where(null);
        for (CustomFilter filter : CustomFilter.valueOf(filter)) {
            Specification<T> filterSpec = getSpecificationForFilter(filter);
            spec = spec.and(filterSpec);
        }
        return spec;
    }

    @JsonIgnore
    private <T> Specification<T> getSpecificationForFilter(CustomFilter filter) {
        Specification<T> spec = Specification.where(null);
        switch (filter.getOperation()) {
            case LIKE:
                spec = (root, query, cb) ->
                        cb.like(cb.lower(getPath(root, filter)),
                                ("%" + filter.getValue() + "%").toLowerCase());
                break;
            default:
                break;
        }
        return spec;
    }

    @JsonIgnore
    private <T> Expression<T> getPath(Root<?> root, CustomFilter filter) {
        String[] items = filter.getField().split("\\.");
        Path<T> path = root.get(items[0]);
        for (int i = 1; i < items.length; i++) {
            path = path.get(items[i]);
        }
        return path;
    }

    public CustomPageRequest() {
        this.page = 0;
        this.size = 10;
        this.sort = "";
        this.filter = Collections.emptyList();
    }
}
