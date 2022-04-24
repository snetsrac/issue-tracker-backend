package com.snetsrac.issuetracker.issue;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Provides CRUD functionality for {@code Issue} entities.
 */
public interface IssueService {
    
    /**
     * Returns a {@link Page} of {@code Issue} entities meeting the paging
     * restriction provided by the {@code Pageable} object.
     * 
     * @param pageable
     * @return A page of {@code Issue} entities
     */
    public Page<Issue> findAll(Pageable pageable);

    /**
     * Retrieves an {@code Issue} entity by its id.
     * 
     * @param id
     * @return The {@code Issue} with the given id or Optional#empty()
     *         if none found
     */
    public Optional<Issue> findById(int id);

    /**
     * Saves a given {@code Issue} entity. Use the returned instance for further
     * operations as the save operation might have changed the issue instance
     * completely.
     * 
     * @param issue must not be null
     * @return The saved issue; will never be null
     * @throws IllegalArgumentException if the given issue is null
     */
    public Issue save(Issue issue);

    /**
     * Deletes the {@code Issue} entity with the given id.
     * 
     * @param id
     */
    public void deleteById(int id);
}
