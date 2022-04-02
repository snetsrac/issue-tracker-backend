package com.snetsrac.issuetracker.issue;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.snetsrac.issuetracker.error.Problem;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PagedDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueMapper mapper;

    // Aggregate root
    @GetMapping
    public ResponseEntity<PagedDto<Issue, IssueDto>> getIssues(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Issue> page = issueRepository.findAll(pageable);
        PagedDto<Issue, IssueDto> dto = PagedDto.from(page, mapper);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> postIssue(@Valid @RequestBody IssueCreationDto dto) {
        Issue issue = mapper.issueCreationDtoToIssue(dto);
        return ResponseEntity.ok(mapper.toDto(issueRepository.save(issue)));
    }

    // Single item
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Object> getIssueById(@PathVariable int id) {
        Optional<Issue> issue = issueRepository.findById(id);
        
        if (issue.isPresent()) {
            return ResponseEntity.ok(mapper.toDto(issue.get()));
        }

        Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @PutMapping("/{id:\\d+}")
    @Transactional
    public ResponseEntity<Object> putIssue(@PathVariable int id, @Valid @RequestBody IssueUpdateDto dto) {
        Optional<Issue> issue = issueRepository.findById(id);
        
        if (issue.isPresent()) {
            mapper.issueUpdateDtoOntoIssue(dto, issue.get());
            return ResponseEntity.ok(mapper.toDto(issueRepository.save(issue.get()))); 
        }

        Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @DeleteMapping("/{id:\\d+}")
    @Transactional
    public ResponseEntity<Object> deleteIssue(@PathVariable int id) {
        Optional<Issue> issue = issueRepository.findById(id);
        
        if (issue.isPresent()) {
            issueRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }
}
