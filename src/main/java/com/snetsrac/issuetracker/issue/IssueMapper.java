package com.snetsrac.issuetracker.issue;

import javax.validation.Valid;

import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.issue.enumerator.IssueStatus;
import com.snetsrac.issuetracker.model.Mapper;

import org.springframework.stereotype.Component;

@Component
public class IssueMapper implements Mapper<Issue, IssueDto> {
    public IssueDto toDto(Issue issue) {
        IssueDto dto = new IssueDto();

        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());
        dto.setStatus(issue.getStatus());
        dto.setPriority(issue.getPriority());

        return dto;
    }

    public Issue issueCreationDtoToIssue(@Valid IssueCreationDto dto) {
        Issue issue = new Issue();

        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(IssueStatus.OPEN);
        issue.setPriority(dto.getPriority());

        return issue;
    }

    public void issueUpdateDtoOntoIssue(@Valid IssueUpdateDto dto, Issue issue) {
        issue.setTitle(dto.getTitle());
        issue.setDescription(dto.getDescription());
        issue.setStatus(dto.getStatus());
        issue.setPriority(dto.getPriority());
    }
}
