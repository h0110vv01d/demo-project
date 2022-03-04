package org.h0110w.som.core.service.util.pagination;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * this class allows you to create an object, some kind of page wrapper that is a response to a CustomPageRequest
 * contains total amount of requested elements stored in database
 * total amount of pages for requested page size
 * and list of requested elements themself
 *
 * @param <T> type of requested object
 * @see CustomPageRequest
 */
@Getter
@Setter
@NoArgsConstructor
public class PagedResult<T> {
    private int totalPages;
    private long totalItems;
    private List<T> items;

    @JsonIgnore
    private Page page;

    public PagedResult(Page<T> page) {
        this(page, page.getContent());
    }

    public PagedResult(Page page, List<T> items) {
        this.page = page;
        this.items = items;
        this.totalItems = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }

    public static <Q> PagedResult<Q> of(Page page, List<Q> items) {
        return new PagedResult<>(page, items);
    }
}
