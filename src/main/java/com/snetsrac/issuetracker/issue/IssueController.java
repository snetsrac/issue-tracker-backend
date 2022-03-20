package com.snetsrac.issuetracker.issue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    @Autowired
    EntityLinks links;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueModelAssembler assembler;

    @Autowired
    private PagedResourcesAssembler<Issue> pagedAssembler;

    // Aggregate root
    @GetMapping
    public ResponseEntity<Object> getIssues(Pageable pageable) {
        Page<Issue> page = issueRepository.findAll(pageable);
        PagedModel<EntityModel<Issue>> model = pagedAssembler.toModel(page, assembler);

        return ResponseEntity.ok(model);
    }

    // Single item
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Object> getIssueById(@PathVariable int id) {
        Optional<Issue> issue = issueRepository.findById(id);
        
        if (issue.isPresent()) {
            return ResponseEntity.ok(assembler.toModel(issue.get()));
        }

        Problem problem = Problem.create()
                .withTitle("Could not find issue " + id + ".")
                .withStatus(HttpStatus.NOT_FOUND)
                .withDetail("The issue does not exist.")
                .withInstance(linkTo(methodOn(IssueController.class).getIssueById(id)).toUri());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> postIssue(@Valid @RequestBody Issue issue) {
        return ResponseEntity.ok(assembler.toModel(issueRepository.save(issue)));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<Object> putIssue(@Valid @RequestBody Issue issue) {
        return ResponseEntity.ok(assembler.toModel(issueRepository.save(issue)));
    }

    @DeleteMapping("/{id:\\d+}")
    @Transactional
    public ResponseEntity<Object> deleteIssue(@PathVariable int id) {
        Optional<Issue> issue = issueRepository.findById(id);
        
        if (!issue.isPresent()) {
            Problem problem = Problem.create()
                    .withTitle("Could not find issue " + id + ".")
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withDetail("The issue does not exist.")
                    .withInstance(linkTo(methodOn(IssueController.class).getIssueById(id)).toUri());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        issueRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
