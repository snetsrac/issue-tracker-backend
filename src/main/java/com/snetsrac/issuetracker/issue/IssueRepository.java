package com.snetsrac.issuetracker.issue;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface IssueRepository extends PagingAndSortingRepository<Issue, Integer> {
    
}
