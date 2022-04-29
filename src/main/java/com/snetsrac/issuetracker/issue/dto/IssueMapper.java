package com.snetsrac.issuetracker.issue.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.snetsrac.issuetracker.issue.Issue;
import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * Provides mapping functions between {@code Issue} entities and issue dtos.
 */
@Component
public class IssueMapper {

    // Prevent initialization
    private IssueMapper() {
    }

    /**
     * Maps an {@link Issue} entity to an {@link IssueDto}. If a user cannot be
     * found in the userMap, {@code null} will be set in its place.
     * 
     * @param issue the issue to be mapped
     * @param users a set of user dtos
     * @return an issue dto
     * @throws IllegalArgumentException if issue or userMap are null
     */
    public static IssueDto toDto(Issue issue, Set<UserDto> userDtos) {

        if (issue == null || userDtos == null) {
            throw new IllegalArgumentException("issue and users must no be null");
        }

        return toDtoInternal(issue, userDtos);
    }

    /**
     * Maps a {@link Page}<{@link Issue}> to a {@link PageDto}<{@link IssueDto}>. If
     * a user cannot be found in the userMap, {@code null} will be set in its place.
     * 
     * @param page  a page of issue
     * @param users a set of user dtos
     * @return a page dto
     * @throws IllegalArgumentException if page or userMap are null
     */
    public static PageDto<IssueDto> toPageDto(Page<Issue> page, Set<UserDto> userDtos) {

        if (page == null || userDtos == null) {
            throw new IllegalArgumentException("page and userMap must not be null");
        }

        // Build the issue dtos
        List<IssueDto> issueDtos = page.getContent().stream()
                .map(issue -> toDtoInternal(issue, userDtos))
                .collect(Collectors.toList());

        // Build the page dto
        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(issueDtos);
        pageDto.setPageMetadata(new PageMetadata(page));

        return pageDto;
    }

    /**
     * Maps an {@link IssueCreationDto} to an {@link Issue} to be persisted to the
     * database. Assumes dto is valid.
     * 
     * @param dto         the dto representing a new issue
     * @param submitterId the user id of the user submitting the issue
     * @return the new issue, ready to be persisted
     * @throws IllegalArgumentException if dto or submitterId are null
     */
    public static Issue toIssue(IssueCreationDto dto, String submitterId) {

        if (dto == null || submitterId == null || submitterId.length() == 0) {
            throw new IllegalArgumentException("dto and submitterId must not be null or empty");
        }

        Issue issue = new Issue();

        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.fromString(dto.getPriority()));
        issue.setSubmitterId(submitterId);

        return issue;
    }

    /**
     * Maps an {@link IssueUpdateDto} onto an existing {@link Issue}, modifying it
     * in-place to be persisted to the database during an update operation. Assumes
     * dto is valid.
     * 
     * @param dto   the dto representing an updated issue
     * @param issue the existing issue
     * @return the original issue, with modifications
     * @throws IllegalArgumentException if dto or issue are null
     */
    public static Issue ontoIssue(@Valid IssueUpdateDto dto, Issue issue) {

        if (dto == null || issue == null) {
            throw new IllegalArgumentException("dto and issue must not be null");
        }

        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(Status.valueOf(dto.getStatus().replaceAll(" ", "_").toUpperCase()));
        issue.setPriority(Priority.valueOf(dto.getPriority().replaceAll(" ", "_").toUpperCase()));

        return issue;
    }

    private static IssueDto toDtoInternal(Issue issue, Set<UserDto> userDtos) {

        // Build a map of user ids to users
        Map<String, UserDto> userMap = userDtos.stream().collect(Collectors.toMap(UserDto::getId, Function.identity()));

        IssueDto dto = new IssueDto();

        if (issue.getId() != null) {
            dto.setId(issue.getId());
        }

        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setStatus(issue.getStatus());
        dto.setPriority(issue.getPriority());
        dto.setSubmitter(userMap.get(issue.getSubmitterId()));
        dto.setAssignees(
                issue.getAssigneeIds()
                        .stream()
                        .map(id -> userMap.get(id))
                        .filter(userDto -> userDto != null)
                        .collect(Collectors.toSet()));

        if (issue.getCreatedAt() != null) {
            dto.setCreatedAt(issue.getCreatedAt().toString());
        }

        return dto;
    }
}
