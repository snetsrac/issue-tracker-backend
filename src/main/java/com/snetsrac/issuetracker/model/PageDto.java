package com.snetsrac.issuetracker.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a page of resources, along with metadata, that will be returned by
 * the server in API responses for endpoints that support pagination.
 */
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof PageDto))
            return false;

        @SuppressWarnings("rawtypes")
        PageDto pageDto = (PageDto) obj;

        return Objects.equals(pageDto.content, this.content) &&
                Objects.equals(pageDto.pageMetadata, this.pageMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, pageMetadata);
    }

    @Override
    public String toString() {
        return "PageDto [content=" + content + ", pageMetadata=" + pageMetadata + "]";
    }
}
