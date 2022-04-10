package com.snetsrac.issuetracker.model;

import java.util.List;

import org.springframework.data.domain.Page;

public class PagedDto<T, D> {
    private List<D> content;
    private PageMetadata<T> page;

    public static <T, D> PagedDto<T, D> from(Page<T> page, Mapper<T, D> mapper) {
        PagedDto<T, D> dto = new PagedDto<>();

        dto.content = page.map(mapper::toDto).getContent();
        dto.page = new PageMetadata<>(page);

        return dto;
    }

    public List<D> getContent() {
        return content;
    }

    public void setContent(List<D> content) {
        this.content = content;
    }

    public PageMetadata<T> getPage() {
        return page;
    }

    public void setPage(PageMetadata<T> page) {
        this.page = page;
    }
}
