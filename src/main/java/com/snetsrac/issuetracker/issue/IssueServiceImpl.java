package com.snetsrac.issuetracker.issue;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public Page<Issue> findAll(Pageable pageable) {
        return issueRepository.findAll(pageable);
    }

    @Override
    public Optional<Issue> findById(int id) {
        return issueRepository.findById(id);
    }

    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    @Override
    public void deleteById(int id) {
        issueRepository.deleteById(id);
    }

}
