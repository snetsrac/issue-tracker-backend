package com.snetsrac.issuetracker.issue;

import javax.validation.Valid;

import com.snetsrac.issuetracker.error.Problem;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
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
    private IssueService issueService;

    @Autowired
    private IssueMapper issueMapper;

    // Aggregate root
    @GetMapping
    public ResponseEntity<PagedDto<Issue, IssueDto>> getIssues(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Issue> page = issueService.findAll(pageable);
        PagedDto<Issue, IssueDto> dto = PagedDto.from(page, issueMapper);

        return ResponseEntity.ok(dto);
    }

    // Single item
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Object> getIssueById(@PathVariable int id) {
        Issue issue = issueService.findById(id);
        
        if (issue == null) {
            Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        return ResponseEntity.ok(issueMapper.toDto(issue));
    }

    @PostMapping
    public ResponseEntity<Object> postIssue(@Valid @RequestBody IssueCreationDto dto) {
        Issue issue = issueMapper.issueCreationDtoToIssue(dto);
        return ResponseEntity.ok(issueMapper.toDto(issueService.save(issue)));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<Object> putIssue(@PathVariable int id, @Valid @RequestBody IssueUpdateDto dto) {
        Issue issue = issueService.findById(id);
        
        if (issue == null) {
            Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        issueMapper.issueUpdateDtoOntoIssue(dto, issue);
        return ResponseEntity.ok(issueMapper.toDto(issueService.save(issue))); 
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Object> deleteIssue(@PathVariable int id) {
        Issue issue = issueService.findById(id);
        
        if (issue == null) {
            Problem problem = new Problem(HttpStatus.NOT_FOUND, "Could not find issue " + id + ".");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
        }

        issueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
