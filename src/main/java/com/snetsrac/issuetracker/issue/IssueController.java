package com.snetsrac.issuetracker.issue;

import com.snetsrac.issuetracker.error.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Issue> pagedAssembler;

    // Aggregate root
    @GetMapping
    public PagedModel<EntityModel<Issue>> getIssues(Pageable pageable) {
        Page<Issue> page = issueRepository.findAll(pageable);

        return pagedAssembler.toModel(page, assembler);
    }

    // Single item
    @GetMapping("/{id:[\\d+]}")
    public EntityModel<Issue> getIssueById(@PathVariable int id) {
        Issue issue = issueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find issue " + id));

        return assembler.toModel(issue);
    }
}
