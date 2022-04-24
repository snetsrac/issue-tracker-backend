package com.snetsrac.issuetracker.issue.dto;

import java.util.List;
import java.util.Map;
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

    /**
     * Maps an {@link Issue} entity to an {@link IssueDto}. If a user cannot be
     * found in the userMap, {@code null} will be set in its place.
     * 
     * @param issue   the issue to be mapped
     * @param userMap a map of user ids to user dtos
     * @return an issue dto
     * @throws IllegalArgumentException if issue or userMap are null
     */
    public IssueDto toDto(Issue issue, Map<String, UserDto> userMap) {

        if (issue == null || userMap == null) {
            throw new IllegalArgumentException("issue and userMap must no be null");
        }

        return toDtoInternal(issue, userMap);
    }

    /**
     * Maps a {@link Page}<{@link Issue}> to a {@link PageDto}<{@link IssueDto}>. If
     * a user cannot be found in the userMap, {@code null} will be set in its place.
     * 
     * @param page a page of issue
     * @param userMap a map of user ids to user dtos
     * @return a page dto
     * @throws IllegalArgumentException if page or userMap are null
     */
    public PageDto<IssueDto> toPageDto(Page<Issue> page, Map<String, UserDto> userMap) {
        if (page == null || userMap == null) {
            throw new IllegalArgumentException("page and userMap must no be null");
        }

        PageDto<IssueDto> pageDto = new PageDto<>();
        List<IssueDto> issueDtos = page.getContent().stream()
                .map(issue -> toDtoInternal(issue, userMap))
                .collect(Collectors.toList());

        pageDto.setContent(issueDtos);
        pageDto.setPageMetadata(new PageMetadata(page));

        return pageDto;
    }

    public Issue issueCreationDtoToIssue(@Valid IssueCreationDto dto, String submitterId) {
        Issue issue = new Issue();

        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.valueOf(dto.getPriority().toUpperCase()));
        issue.setSubmitterId(submitterId);

        return issue;
    }

    public Issue issueUpdateDtoOntoIssue(@Valid IssueUpdateDto dto, Issue issue) {
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(Status.valueOf(dto.getStatus().replaceAll(" ", "_").toUpperCase()));
        issue.setPriority(Priority.valueOf(dto.getPriority().replaceAll(" ", "_").toUpperCase()));

        return issue;
    }

    private IssueDto toDtoInternal(Issue issue, Map<String, UserDto> userMap) {
        IssueDto dto = new IssueDto();

        dto.setId(issue.getId());
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
        dto.setCreatedAt(issue.getCreatedAt().toString());

        return dto;
    }
}
