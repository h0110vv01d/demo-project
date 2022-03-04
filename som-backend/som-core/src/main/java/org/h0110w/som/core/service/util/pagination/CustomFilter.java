package org.h0110w.som.core.service.util.pagination;

import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * helper class used in cases when we interact with a database and we need to filter objects by a specific criteria
 * converts raw filter string into custom object (CustomFilter) with which we can further work
 * currently supports two operation EQ and LIKE
 */
@Getter
public class CustomFilter {
    private final String field;
    private final Operation operation;
    private final String value;

    public CustomFilter(String field, Operation operation, String value) {
        this.field = field;
        this.operation = operation;
        this.value = value;
    }

    public enum Operation {
        EQ, LIKE
    }

    public static List<CustomFilter> valueOf(List<String> rawFilters) {
        List<CustomFilter> filters = new ArrayList<>();
        rawFilters.forEach(rawFilter -> {
            CustomFilter filter = valueOf(rawFilter);
            if (filter != null) {
                filters.add(filter);
            }
        });
        return filters;
    }

    public static CustomFilter valueOf(String rawFilter) {
        if (StringUtils.isEmpty(rawFilter)) {
            return null;
        }
        String[] items = rawFilter.split(":", 3);
        if (items.length != 3) {
            throw new IllegalArgumentException("invalid filter");
        }
        Arrays.stream(items).forEach(item ->
        {
            if (StringUtils.isEmpty(item)) {
                throw new IllegalArgumentException("invalid filter");
            }
        });
        String filed = items[0];
        Operation operation = Operation.valueOf(items[1].toUpperCase());
        String value = items[2];
        return new CustomFilter(filed, operation, value);
    }
}
