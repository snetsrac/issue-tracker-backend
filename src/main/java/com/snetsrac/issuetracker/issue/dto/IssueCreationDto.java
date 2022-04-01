package com.snetsrac.issuetracker.issue.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;

public class IssueCreationDto {
    @NotBlank(message = "Issue title is required.")
    private String title;

    @NotBlank(message = "Issue description is required.")
    private String description;

    @NotNull(message = "Issue priority is required.")
    private IssuePriority priority;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public IssuePriority getPriority() {
        return priority;
    }
    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }
}
