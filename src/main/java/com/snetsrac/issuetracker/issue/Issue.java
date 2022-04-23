package com.snetsrac.issuetracker.issue;

import java.time.Instant;
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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.snetsrac.issuetracker.model.BaseEntity;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "issue")
public class Issue extends BaseEntity {
    
    public enum Status {
        @JsonProperty("open")
        OPEN,
        @JsonProperty("in progress")
        IN_PROGRESS,
        @JsonProperty("more info needed")
        MORE_INFO_NEEDED,
        @JsonProperty("resolved")
        RESOLVED
    }
    
    public enum Priority {
        @JsonProperty("high")
        HIGH,
        @JsonProperty("medium")
        MEDIUM,
        @JsonProperty("low")
        LOW
    }

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Type(type = "postgresql_enum")
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Type(type = "postgresql_enum")
    @Column(name = "priority")
    private Priority priority;

    @Column(name = "submitter_id")
    private String submitterId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "issue_assignee", joinColumns = @JoinColumn(name = "issue_id"))
    @Column(name = "user_id")
    private Set<String> assigneeIds;

    @CreatedDate
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

}
