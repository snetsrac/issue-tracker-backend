package com.snetsrac.issuetracker.issue.dto;

import java.util.List;

import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;
import com.snetsrac.issuetracker.user.dto.UserDto;

public class IssueDto {
    private int id;
    private String title;
    private String description;
    private IssueStatus status;
    private IssuePriority priority;
    private UserDto submitter;
    private List<UserDto> assignees;

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

    public IssueStatus getStatus() {
        return status;
    }

    public void setStatus(IssueStatus status) {
        this.status = status;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
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
}
