package com.snetsrac.issuetracker.issue;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Provides CRUD functionality for {@code Issue} entities.
 */
@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    public IssueServiceImpl(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    /**
     * Returns a {@link Page} of {@code Issue} entities meeting the paging
     * restriction provided by the {@code Pageable} object.
     * 
     * @param pageable
     * @return A page of {@code Issue} entities
     */
    @Override
    public Page<Issue> findAll(Pageable pageable) {
        return issueRepository.findAll(pageable);
    }

    /**
     * Retrieves an {@code Issue} entity by its id.
     * 
     * @param id
     * @return The {@code Issue} with the given id or Optional#empty()
     *         if none found
     */
    @Override
    public Optional<Issue> findById(int id) {
        return issueRepository.findById(id);
    }

    /**
     * Saves a given {@code Issue} entity. Use the returned instance for further
     * operations as the save operation might have changed the issue instance
     * completely.
     * 
     * @param issue must not be null
     * @return The saved issue; will never be null
     * @throws IllegalArgumentException if the given issue is null
     */
    @Override
    public Issue save(Issue issue) {
        return issueRepository.save(issue);
    }

    /**
     * Deletes the {@code Issue} entity with the given id.
     * 
     * @param id
     */
    @Override
    public void deleteById(int id) {
        issueRepository.deleteById(id);
    }

}
