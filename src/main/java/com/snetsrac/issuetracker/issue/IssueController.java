package com.snetsrac.issuetracker.issue;

import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.error.BadRequestException;
import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IssueController.ENDPOINT)
public class IssueController {

    public static final String ENDPOINT = "/issues";

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private UserService userService;

    // Aggregate root
    @GetMapping
    @PreAuthorize("hasAuthority('read:issues')")
    public PageDto<IssueDto> getIssues(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<Issue> page = issueService.findAll(pageable);
        Set<String> userIds = getUserIds(page);
        Map<String, User> userMap = userService.findByIds(userIds);
        PageDto<IssueDto> dto = issueMapper.toPagedDto(page, userMap);
        return dto;
    }

    // Single item
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:issues')")
    public IssueDto getIssueById(@PathVariable String id) {
        Issue issue = issueService.findById(convertPathVariableToInt(id));
        
        if (issue == null) {
            throw new NotFoundException("issue.not-found");
        }

        Set<String> userIds = getUserIds(issue);
        Map<String, User> userMap = userService.findByIds(userIds);
        return issueMapper.toDto(issue, userMap);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('submit:issues')")
    public ResponseEntity<IssueDto> postIssue(@RequestBody @Valid IssueCreationDto dto, Authentication auth) {
        String userId = ((JwtAuthenticationToken) auth).getToken().getSubject();
        Issue issue = issueService.save(issueMapper.issueCreationDtoToIssue(dto, userId));
        URI location = URI.create(ENDPOINT + issue.getId());

        Set<String> userIds = getUserIds(issue);
        Map<String, User> userMap = userService.findByIds(userIds);
        return ResponseEntity.created(location).body(issueMapper.toDto(issue, userMap));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('submit:issues', 'modify:issues')")
    public IssueDto putIssue(@PathVariable String id, @RequestBody @Valid IssueUpdateDto dto) {
        Issue issue = issueService.findById(convertPathVariableToInt(id));
        
        if (issue == null) {
            throw new NotFoundException("issue.not-found");
        }

        issue = issueService.save(issueMapper.issueUpdateDtoOntoIssue(dto, issue));

        Set<String> userIds = getUserIds(issue);
        Map<String, User> userMap = userService.findByIds(userIds);
        return issueMapper.toDto(issue, userMap);
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

    private Set<String> getUserIds(Page<Issue> page) {
        Set<String> userIds = new HashSet<>();

        for (Issue issue : page.getContent()) {
            userIds.add(issue.getSubmitterId());
        }

        return userIds;
    }

    private Set<String> getUserIds(Issue issue) {
        Set<String> userIds = new HashSet<>();

        userIds.add(issue.getSubmitterId());

        return userIds;
    }
}
