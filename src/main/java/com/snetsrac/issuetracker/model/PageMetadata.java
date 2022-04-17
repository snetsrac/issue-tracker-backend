package com.snetsrac.issuetracker.model;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.springframework.data.domain.Sort;

@JsonInclude(Include.NON_NULL)
public class PageMetadata<T> {
    private Integer size;
    private Long totalElements;
    private Integer totalPages;
    private Integer number;
    private List<String> sort;

    public PageMetadata(org.springframework.data.domain.Page<T> page) {
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.sort = page.getSort().toList().stream().map(PageMetadata::sortToString).collect(Collectors.toList());

    }

    public PageMetadata(com.auth0.json.mgmt.Page<T> page) {
        // If the query summary is included and valid, use it, otherwise use the list
        // size as a fallback. The summary will not be included unless .withTotals(true)
        // is added to the UserFilter.
        if (page != null && page.getLimit() != null && page.getTotal() != null && page.getStart() != null
                && page.getLimit() > 0) {
            this.size = page.getLimit();
            this.totalElements = page.getTotal().longValue();
            this.totalPages = ((this.totalElements.intValue() - 1) / this.size) + 1;
            this.number = page.getStart() / this.size;
        } else {
            this.size = page.getItems().size();
        }
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Long totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    private static String sortToString(Sort.Order order) {
        return order.getProperty() + "," + order.getDirection().name().toLowerCase()
                + (order.isIgnoreCase() ? ",ignorecase" : "");
    }
}
