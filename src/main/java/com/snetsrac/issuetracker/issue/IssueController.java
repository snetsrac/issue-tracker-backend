package com.snetsrac.issuetracker.issue;

import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.snetsrac.issuetracker.error.BadRequestException;
import com.snetsrac.issuetracker.error.NotFoundException;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.user.UserService;
import com.snetsrac.issuetracker.user.dto.UserDto;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    private final IssueService issueService;
    private final UserService userService;

    public IssueController(IssueService issueService, UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
    }

    /**
     * Handles GET requests to the issues endpoint.
     * 
     * @param pageable pagination options
     * @returns a page of issues
     */
    @GetMapping
    @PreAuthorize("hasAuthority('read:issues')")
    public PageDto<IssueDto> getIssues(@PageableDefault(size = 20, sort = "id") Pageable pageable) {

        // Get a page of issues from the database
        Page<Issue> issuePage = issueService.findAll(pageable);

        // Get all relevant users and build a set of user dtos
        Set<UserDto> userDtos = userService.findByIds(getUserIds(issuePage))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());

        // Build a page dto and return 200
        return IssueMapper.toPageDto(issuePage, userDtos);
    }

    /**
     * Handles POST requests to the issues endpoint.
     * 
     * @param dto the issue creation request body
     * @param auth the Spring Security {@code Authentication}
     * @return the new issue
     * @throws BadRequestException if the request body is invalid
     */
    @PostMapping
    @PreAuthorize("hasAuthority('submit:issues')")
    public ResponseEntity<IssueDto> postIssue(@RequestBody @Valid IssueCreationDto dto, Authentication auth) {

        // Save the issue to the database
        Issue issue = issueService.save(IssueMapper.toIssue(dto, auth.getName()));

        // Get the path to the new issue
        URI location = URI.create(ENDPOINT + "/" + issue.getId());
        
        // Get all relevant users and build a set of user dtos
        Set<UserDto> userDtos = userService.findByIds(getUserIds(issue))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());

        // Build the dto and return 201 with a location header
        IssueDto responseDto = IssueMapper.toDto(issue, userDtos);
        return ResponseEntity.created(location).body(responseDto);
    }

    /**
     * Handles GET requests to the issues/:id endpoint.
     * 
     * @param id the id of the issue to retrieve
     * @returns the issue
     * @throws NotFoundException if the issue doesn't exist
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:issues')")
    public IssueDto getIssueById(@PathVariable String id) {
        
        // Get the issue from the database
        Optional<Issue> issue = issueService.findById(convertPathVariableToInt(id));
        
        // If no issue was found, return 404
        if (!issue.isPresent()) {
            throw new NotFoundException("issue.not-found");
        }

        // Get all relevant users and build a set of user dtos
        Set<UserDto> userDtos = userService.findByIds(getUserIds(issue.get()))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());

        // Build a dto and return 200
        return IssueMapper.toDto(issue.get(), userDtos);
    }

    /**
     * Handles PUT requests to the issues/:id endpoint.
     * 
     * @param id the id of the issue to update
     * @param dto the issue update request body
     * @return the new issue
     * @throws BadRequestException if the request body is invalid
     * @throws NotFoundException if the issue doesn't exist
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('modify:issues') or (hasAuthority('submit:issues') and #id == authentication.name)")
    public IssueDto putIssue(@PathVariable String id, @RequestBody @Valid IssueUpdateDto dto) {

        // Get the issue from the database
        Optional<Issue> existingIssue = issueService.findById(convertPathVariableToInt(id));
        
        // If no issue was found, return 404
        if (!existingIssue.isPresent()) {
            throw new NotFoundException("issue.not-found");
        }

        // Perform the updates and save to the database
        Issue updatedIssue = IssueMapper.ontoIssue(dto, existingIssue.get());
        updatedIssue = issueService.save(updatedIssue);

        // Get all relevant users and build a set of user dtos
        Set<UserDto> userDtos = userService.findByIds(getUserIds(updatedIssue))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toSet());

        // Build a page dto and return 200
        return IssueMapper.toDto(updatedIssue, userDtos);
    }

    /**
     * Handles DELETE requests to the issues/:id endpoint.
     * 
     * @param id the id of the issue to delete
     * @throws NotFoundException if the issue doesn't exist
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:issues')")
    public ResponseEntity<Object> deleteIssue(@PathVariable String id) {

        // First, check that the issue exists
        int issueId = convertPathVariableToInt(id);
        Optional<Issue> issue = issueService.findById(convertPathVariableToInt(id));
        
        // If it doesn't, return 404
        if (!issue.isPresent()) {
            throw new NotFoundException("issue.not-found");
        }

        // Delete the issue and return 204
        issueService.deleteById(issueId);
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
            userIds.addAll(issue.getAssigneeIds());
        }

        return userIds;
    }

    private Set<String> getUserIds(Issue issue) {
        Set<String> userIds = new HashSet<>();

        userIds.add(issue.getSubmitterId());
        userIds.addAll(issue.getAssigneeIds());

        return userIds;
    }
}
