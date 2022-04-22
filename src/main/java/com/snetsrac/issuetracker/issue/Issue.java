package com.snetsrac.issuetracker.issue;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;
import com.snetsrac.issuetracker.model.BaseEntity;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "issue")
public class Issue extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Type(type = "postgresql_enum")
    @Column(name = "status")
    private IssueStatus status;

    @Enumerated(EnumType.STRING)
    @Type(type = "postgresql_enum")
    @Column(name = "priority")
    private IssuePriority priority;

    @Column(name = "submitter_id")
    private String submitterId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "issue_assignee", joinColumns = @JoinColumn(name = "issue_id"))
    @Column(name = "user_id")
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

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public Set<String> getAssigneeIds() {
        return assigneeIds;
    }

    public void setAssigneeIds(Set<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }
}
