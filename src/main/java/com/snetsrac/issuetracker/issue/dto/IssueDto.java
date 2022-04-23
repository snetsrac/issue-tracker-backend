package com.snetsrac.issuetracker.issue.dto;

import java.util.List;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.user.dto.UserDto;

public class IssueDto {
    
    private int id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private UserDto submitter;
    private List<UserDto> assignees;
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

    public List<UserDto> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<UserDto> assignees) {
        this.assignees = assignees;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
