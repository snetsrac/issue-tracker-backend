package com.snetsrac.issuetracker.issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {
    public Page<Issue> findAll(Pageable pageable);

    public Issue save(Issue issue);

    public Issue findById(int id);

    public void deleteById(int id);
}
