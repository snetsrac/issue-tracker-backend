package com.snetsrac.issuetracker.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDto<D> {
    private List<D> content;
    private PageMetadata pageMetadata;

    public List<D> getContent() {
        return content;
    }

    public void setContent(List<D> content) {
        this.content = content;
    }

    @JsonProperty("page")
    public PageMetadata getPageMetadata() {
        return pageMetadata;
    }

    public void setPageMetadata(PageMetadata pageMetadata) {
        this.pageMetadata = pageMetadata;
    }
}
