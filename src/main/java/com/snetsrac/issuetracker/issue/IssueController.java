package com.snetsrac.issuetracker.issue;

import java.net.URI;

import javax.validation.Valid;

import com.snetsrac.issuetracker.error.BadRequestException;
import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PagedDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('read:issues')")
    public PagedDto<Issue, IssueDto> getIssues(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Issue> page = issueService.findAll(pageable);
        return PagedDto.from(page, issueMapper);
    }

    // Single item
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:issues')")
    public IssueDto getIssueById(@PathVariable String id) {
        Issue issue = issueService.findById(convertPathVariableToInt(id));
        
        if (issue == null) {
            throw new NotFoundException("issue.not-found");
        }

        return issueMapper.toDto(issue);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('write:issues')")
    public ResponseEntity<IssueDto> postIssue(@RequestBody @Valid IssueCreationDto dto) {
        Issue issue = issueService.save(issueMapper.issueCreationDtoToIssue(dto));
        return ResponseEntity.created(URI.create("/issues/" + issue.getId())).body(issueMapper.toDto(issue));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('write:issues')")
    public IssueDto putIssue(@PathVariable String id, @RequestBody @Valid IssueUpdateDto dto) {
        Issue issue = issueService.findById(convertPathVariableToInt(id));
        
        if (issue == null) {
            throw new NotFoundException("issue.not-found");
        }

        issueMapper.issueUpdateDtoOntoIssue(dto, issue);
        return issueMapper.toDto(issueService.save(issue));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:issues')")
    public ResponseEntity<Object> deleteIssue(@PathVariable String id) {
        int idInt = convertPathVariableToInt(id);
        Issue issue = issueService.findById(idInt);
        
        if (issue == null) {
            throw new NotFoundException("issue.not-found");
        }

        issueService.deleteById(idInt);
        return ResponseEntity.noContent().build();
    }

    private int convertPathVariableToInt(String s) {
        try {
            int x = Integer.parseInt(s);

            if (x < 0) {
                throw new BadRequestException("issue.id.valid");
            }

            return x;
        } catch (NumberFormatException e) {
            throw new BadRequestException("issue.id.valid");
        }
    }
}
