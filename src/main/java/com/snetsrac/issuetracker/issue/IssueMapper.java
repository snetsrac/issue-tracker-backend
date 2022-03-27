package com.snetsrac.issuetracker.issue;

import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.model.Mapper;

import org.springframework.stereotype.Component;

@Component
public class IssueMapper implements Mapper<Issue, IssueDto> {
    public IssueDto toDto(Issue issue) {
        IssueDto dto = new IssueDto();

        dto.setId(issue.getId());
        dto.setTitle(issue.getTitle());
        dto.setDescription(issue.getDescription());

        return dto;
    }
}
