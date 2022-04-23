package com.snetsrac.issuetracker.issue.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.model.EnumString;

public class IssueUpdateDto {

    @NotBlank(message = "{issue.title.required}")
    private String title;

    @NotBlank(message = "{issue.description.required}")
    private String description;

    @NotBlank(message = "{issue.status.required}")
    @EnumString(enumClass = Status.class, message = "{issue.status.valid}")
    private String status;

    @NotBlank(message = "{issue.priority.required}")
    @EnumString(enumClass = Priority.class, message = "{issue.priority.valid}")
    private String priority;

    private Set<String> assigneeIds;

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

    public Set<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(Set<String> assigneeIds) {
        if (assigneeIds == null) {
            this.assigneeIds = new HashSet<>();
        } else {
            this.assigneeIds = assigneeIds;
        }
    }

}
