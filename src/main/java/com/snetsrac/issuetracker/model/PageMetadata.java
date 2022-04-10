package com.snetsrac.issuetracker.model;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public class PageMetadata<T> {
    private int size;
    private long totalElements;
    private int totalPages;
    private int number;
    private List<String> sort;

    public PageMetadata(Page<T> page) {
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.sort = page.getSort().toList().stream().map(PageMetadata::sortToString).collect(Collectors.toList());

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

    private static String sortToString(Sort.Order order) {
        return order.getProperty() + "," + order.getDirection().name().toLowerCase()
                + (order.isIgnoreCase() ? ",ignorecase" : "");
    }
}
