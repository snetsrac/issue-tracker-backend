package com.snetsrac.issuetracker.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class PagedDto<T, D> {
    private List<D> content;
    private int size;
    private long totalElements;
    private int totalPages;
    private int number;
    private List<String> sort;

    public static <T, D> PagedDto<T, D> from(Page<T> page, Mapper<T, D> mapper) {
        PagedDto<T, D> dto = new PagedDto<>();

        dto.content = page.map(mapper::toDto).getContent();
        dto.size = page.getSize();
        dto.totalElements = page.getTotalElements();
        dto.totalPages = page.getTotalPages();
        dto.number = page.getNumber();
        dto.sort = page.getSort().toList().stream().map(PagedDto::sortToString).collect(Collectors.toList());

        return dto;
    }

    private static String sortToString(Sort.Order order) {
        return order.getProperty() + "," + order.getDirection().name().toLowerCase()
                + (order.isIgnoreCase() ? ",ignorecase" : "");
    }

    public List<D> getContent() {
        return content;
    }

    public void setContent(List<D> content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }
}
