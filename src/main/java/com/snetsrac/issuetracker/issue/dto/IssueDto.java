package com.snetsrac.issuetracker.issue.dto;

import java.util.Objects;
import java.util.Set;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.user.dto.UserDto;

/**
 * Represents the {@code Issue} object that will be returned by the server in
 * API responses.
 */
public class IssueDto {

    private int id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UserDto submitter;
    private Set<UserDto> assignees;
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public UserDto getSubmitter() {
        return submitter;
    }

    public void setSubmitter(UserDto submitter) {
        this.submitter = submitter;
    }

    public Set<UserDto> getAssignees() {
        return assignees;
    }

    public void setAssignees(Set<UserDto> assignees) {
        this.assignees = assignees;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof IssueDto))
            return false;

        IssueDto issueDto = (IssueDto) obj;

        return Objects.equals(issueDto.id, this.id) &&
                Objects.equals(issueDto.title, this.title) &&
                Objects.equals(issueDto.description, this.description) &&
                Objects.equals(issueDto.status, this.status) &&
                Objects.equals(issueDto.priority, this.priority) &&
                Objects.equals(issueDto.submitter, this.submitter) &&
                Objects.equals(issueDto.assignees, this.assignees) &&
                Objects.equals(issueDto.createdAt, this.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, priority, submitter, assignees, createdAt);
    }

    @Override
    public String toString() {
        return "IssueDto [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
                + ", priority=" + status + ", submitter=" + submitter + ", assignees=" + assignees + ", createdAt="
                + createdAt + "]";
    }

}
