package com.snetsrac.issuetracker.issue.dto;

import javax.validation.constraints.NotBlank;

import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;
import com.snetsrac.issuetracker.model.EnumString;

public class IssueUpdateDto {
    @NotBlank(message = "{issue.title.required}")
    private String title;

    @NotBlank(message = "{issue.description.required}")
    private String description;

    @NotBlank(message = "{issue.status.required}")
    @EnumString(enumClass = IssueStatus.class, message = "{issue.status.valid}")
    private String status;

    @NotBlank(message = "{issue.priority.required")
    @EnumString(enumClass = IssuePriority.class, message = "{issue.priority.valid}")
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
