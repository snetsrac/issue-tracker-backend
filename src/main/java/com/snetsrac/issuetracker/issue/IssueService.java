package com.snetsrac.issuetracker.issue;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {
    public Page<Issue> findAll(Pageable pageable);

    public Optional<Issue> findById(int id);

    public Issue save(Issue issue);

    public void deleteById(int id);
}
