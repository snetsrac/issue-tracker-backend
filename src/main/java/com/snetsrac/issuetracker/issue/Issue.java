package com.snetsrac.issuetracker.issue;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.snetsrac.issuetracker.error.EnumValueNotPresentException;
import com.snetsrac.issuetracker.model.BaseEntity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * JPA entity representing an issue.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "issue")
public class Issue extends BaseEntity {

    public enum Status {
        OPEN("open"),
        IN_PROGRESS("in progress"),
        MORE_INFO_NEEDED("more info needed"),
        RESOLVED("resolved");

        private String value;

        @JsonCreator
        Status(String value) {
            this.value = value;
        }

        @JsonValue
        public String toString() {
            return value;
        }

        public static Status fromString(String input) {
            for (Status status : values()) {
                if (status.toString().equals(input)) {
                    return status;
                }
            }

            throw new EnumValueNotPresentException("Not a valid status: " + input);
        }
    }

    public enum Priority {
        HIGH("high"),
        MEDIUM("medium"),
        LOW("low");

        private String value;

        @JsonCreator
        Priority(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return value;
        }

        public static Priority fromString(String input) {
            for (Priority priority : values()) {
                if (priority.toString().equals(input)) {
                    return priority;
                }
            }

            throw new EnumValueNotPresentException("Not a valid priority: " + input);
        }
    }

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "submitter_id")
    private String submitterId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "issue_assignee", joinColumns = @JoinColumn(name = "issue_id"))
    @Column(name = "user_id", nullable = false)
    private Set<String> assigneeIds;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

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

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public Set<String> getAssigneeIds() {
        if (assigneeIds == null) {
            assigneeIds = new HashSet<>();
        }

        return assigneeIds;
    }

    public void setAssigneeIds(Set<String> assigneeIds) {
        this.assigneeIds = assigneeIds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Issue))
            return false;

        Issue issue = (Issue) obj;

        return Objects.equals(issue.id, this.id) &&
                Objects.equals(issue.title, this.title) &&
                Objects.equals(issue.description, this.description) &&
                Objects.equals(issue.status, this.status) &&
                Objects.equals(issue.priority, this.priority) &&
                Objects.equals(issue.submitterId, this.submitterId) &&
                Objects.equals(issue.assigneeIds, this.assigneeIds) &&
                Objects.equals(issue.createdAt, this.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, priority, submitterId, assigneeIds, createdAt);
    }

    @Override
    public String toString() {
        return "IssueDto [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
                + ", priority=" + status + ", submitterId=" + submitterId + ", assigneeIds=" + assigneeIds
                + ", createdAt=" + createdAt + "]";
    }

}
