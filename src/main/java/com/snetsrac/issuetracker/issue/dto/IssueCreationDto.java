package com.snetsrac.issuetracker.issue.dto;

import javax.validation.constraints.NotBlank;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.model.EnumString;

public class IssueCreationDto {
    @NotBlank(message = "{issue.title.required}")
    private String title;

    @NotBlank(message = "{issue.description.required}")
    private String description;

    @NotBlank(message = "{issue.priority.required}")
    @EnumString(enumClass = Priority.class, message = "{issue.priority.valid}")
    private String priority;

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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
