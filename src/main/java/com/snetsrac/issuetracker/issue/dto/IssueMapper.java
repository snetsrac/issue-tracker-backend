package com.snetsrac.issuetracker.issue.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.issue.Issue;
import com.snetsrac.issuetracker.issue.enumerator.IssuePriority;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.user.UserService;
import com.snetsrac.issuetracker.user.dto.UserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class IssueMapper {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    public IssueDto toDto(Issue issue, Map<String, User> userMap) {
        if (issue == null || userMap == null) {
            return null;
        }

        return toDtoInternal(issue, userMap);
    }

    public PageDto<IssueDto> toPagedDto(Page<Issue> page, Map<String, User> userMap) {
        if (page == null || userMap == null) {
            return null;
        }

        PageDto<IssueDto> pagedDto = new PageDto<>();
        List<IssueDto> issueDtos = new ArrayList<>();

        for (Issue issue : page.getContent()) {
            issueDtos.add(toDtoInternal(issue, userMap));
        }

        pagedDto.setContent(issueDtos);
        pagedDto.setPageMetadata(new PageMetadata(page));

        return pagedDto;
    }

    public Issue issueCreationDtoToIssue(@Valid IssueCreationDto dto, String submitterId) {
        Issue issue = new Issue();

        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(IssueStatus.OPEN);
        issue.setPriority(IssuePriority.valueOf(dto.getPriority().toUpperCase()));
        issue.setSubmitterId(submitterId);

        return issue;
    }

    public Issue issueUpdateDtoOntoIssue(@Valid IssueUpdateDto dto, Issue issue) {
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(IssueStatus.valueOf(dto.getStatus().toUpperCase()));
        issue.setPriority(IssuePriority.valueOf(dto.getPriority().toUpperCase()));

        return issue;
    }

    private IssueDto toDtoInternal(Issue issue, Map<String, User> userMap) {
        IssueDto dto = new IssueDto();

        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setStatus(issue.getStatus());
        dto.setPriority(issue.getPriority());
        dto.setSubmitter(userMapper.toDto(userMap.get(issue.getSubmitterId())));
        dto.setAssignees(
                issue.getAssigneeIds()
                        .stream()
                        .map(id -> userMapper.toDto(userMap.get(id)))
                        .collect(Collectors.toList()));

        return dto;
    }
}
