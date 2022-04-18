package com.snetsrac.issuetracker.issue;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    public Issue() {
    }

    public Issue(String title, String description, IssueStatus status, IssuePriority priority, String submitterId) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.submitterId = submitterId;
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

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.title, this.description, this.status, this.priority, this.submitterId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof Issue))
            return false;

        Issue issue = (Issue) obj;
        return Objects.equals(this.getId(), issue.getId()) && Objects.equals(this.title, issue.title) &&
                Objects.equals(this.description, issue.description) && Objects.equals(this.status, issue.status) &&
                Objects.equals(this.priority, issue.priority) && Objects.equals(this.submitterId, issue.submitterId);
    }

    @Override
    public String toString() {
        return "Issue [id=" + this.getId() + ", title=" + title + ", description=" + description + ", status=" +
                status + ", priority=" + priority + ", submitterId=" + submitterId + "]";
    }
}
