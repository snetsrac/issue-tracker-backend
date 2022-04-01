package com.snetsrac.issuetracker.issue.enumerator;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum IssueStatus {
    @JsonProperty("open")
    OPEN,
    @JsonProperty("in progress")
    IN_PROGRESS,
    @JsonProperty("more info needed")
    MORE_INFO_NEEDED,
    @JsonProperty("resolved")
    RESOLVED
}
