package com.snetsrac.issuetracker.issue;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "issue")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    public Issue() {
    }

    public Issue(String title, String description) {
        this.title = title;
        this.description = description;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj instanceof Issue))
            return false;

        Issue issue = (Issue) obj;
        return Objects.equals(this.id, issue.id) && Objects.equals(this.title, issue.title) &&
                Objects.equals(this.description, issue.description);
    }

    @Override
    public String toString() {
        return "Issue [id=" + id + ", title=" + title + ", description=" + description + "]";
    }
}