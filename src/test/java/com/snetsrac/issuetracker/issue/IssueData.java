package com.snetsrac.issuetracker.issue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.auth0.json.mgmt.users.User;
import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.UserData;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class IssueData {

    public static List<Issue> issueList() {
        List<User> users = UserData.userList();

        List<Issue> issues = new ArrayList<>();
        issues.add(new Issue());
        issues.add(new Issue());
        issues.add(new Issue());

        issues.get(0).setId(123);
        issues.get(0).setTitle("Test Issue 1");
        issues.get(0).setDescription("This is the first test issue.");
        issues.get(0).setStatus(Status.OPEN);
        issues.get(0).setPriority(Priority.HIGH);
        issues.get(0).setSubmitterId(users.get(0).getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(users.get(0).getId());
        issue0Assignees.add(users.get(1).getId());
        issue0Assignees.add(users.get(2).getId());
        issues.get(0).setAssigneeIds(issue0Assignees);
        issues.get(0).setCreatedAt(Instant.EPOCH.plusSeconds(1));

        issues.get(1).setId(456);
        issues.get(1).setTitle("Test Issue 2");
        issues.get(1).setDescription("This is the second test issue.");
        issues.get(1).setStatus(Status.OPEN);
        issues.get(1).setPriority(Priority.HIGH);
        issues.get(1).setSubmitterId(null);
        Set<String> issue1Assignees = new LinkedHashSet<>();
        issue1Assignees.add(users.get(1).getId());
        issue1Assignees.add(users.get(2).getId());
        issues.get(1).setAssigneeIds(issue1Assignees);
        issues.get(1).setCreatedAt(Instant.EPOCH.plusSeconds(2));

        issues.get(2).setId(789);
        issues.get(2).setTitle("Test Issue 3");
        issues.get(2).setDescription("This is the third test issue.");
        issues.get(2).setStatus(Status.OPEN);
        issues.get(2).setPriority(Priority.HIGH);
        issues.get(2).setSubmitterId(users.get(0).getId());
        issues.get(2).setAssigneeIds(new LinkedHashSet<>());
        issues.get(2).setCreatedAt(Instant.EPOCH.plusSeconds(3));

        return issues;
    }

    public static List<IssueDto> issueDtoList() {
        List<UserDto> userDtos = UserData.userDtoList();

        List<IssueDto> issueDtos = new ArrayList<>();
        issueDtos.add(new IssueDto());
        issueDtos.add(new IssueDto());
        issueDtos.add(new IssueDto());

        issueDtos.get(0).setId(123);
        issueDtos.get(0).setTitle("Test Issue 1");
        issueDtos.get(0).setDescription("This is the first test issue.");
        issueDtos.get(0).setStatus(Status.OPEN);
        issueDtos.get(0).setPriority(Priority.HIGH);
        issueDtos.get(0).setSubmitter(userDtos.get(0));
        Set<UserDto> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(userDtos.get(0));
        issue0Assignees.add(userDtos.get(1));
        issue0Assignees.add(userDtos.get(2));
        issueDtos.get(0).setAssignees(issue0Assignees);
        issueDtos.get(0).setCreatedAt(Instant.EPOCH.plusSeconds(1).toString());

        issueDtos.get(1).setId(456);
        issueDtos.get(1).setTitle("Test Issue 2");
        issueDtos.get(1).setDescription("This is the second test issue.");
        issueDtos.get(1).setStatus(Status.OPEN);
        issueDtos.get(1).setPriority(Priority.HIGH);
        issueDtos.get(1).setSubmitter(null);
        Set<UserDto> issue1Assignees = new LinkedHashSet<>();
        issue1Assignees.add(userDtos.get(1));
        issue1Assignees.add(userDtos.get(2));
        issueDtos.get(1).setAssignees(issue1Assignees);
        issueDtos.get(1).setCreatedAt(Instant.EPOCH.plusSeconds(2).toString());

        issueDtos.get(2).setId(789);
        issueDtos.get(2).setTitle("Test Issue 3");
        issueDtos.get(2).setDescription("This is the third test issue.");
        issueDtos.get(2).setStatus(Status.OPEN);
        issueDtos.get(2).setPriority(Priority.HIGH);
        issueDtos.get(2).setSubmitter(userDtos.get(0));
        issueDtos.get(2).setAssignees(Set.of());
        issueDtos.get(2).setCreatedAt(Instant.EPOCH.plusSeconds(3).toString());

        return issueDtos;
    }

    public static Page<Issue> issuePage() {
        return new PageImpl<>(issueList());
    }

    public static PageDto<IssueDto> issuePageDto() {
        PageDto<IssueDto> pageDto = new PageDto<>();

        pageDto.setContent(issueDtoList());
        pageDto.setPageMetadata(new PageMetadata(issuePage()));

        return pageDto;
    }

    public static IssueCreationDto issueCreationDto() {
        IssueCreationDto dto = new IssueCreationDto();

        dto.setTitle("Test Issue");
        dto.setDescription("This is a test issue.");
        dto.setPriority(Priority.MEDIUM.toString());

        return dto;
    }

    public static Issue issueCreationIssue() {
        Issue issue = new Issue();

        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.MEDIUM);
        issue.setSubmitterId("123");

        return issue;
    }

    public static IssueUpdateDto issueUpdateDto() {
        IssueUpdateDto dto = new IssueUpdateDto();

        dto.setTitle("Modified Test Issue");
        dto.setDescription("This is a modified test issue.");
        dto.setStatus(Status.MORE_INFO_NEEDED.toString());
        dto.setPriority(Priority.LOW.toString());

        return dto;
    }

    public static Issue issueUpdateIssue() {
        List<User> users = UserData.userList();
        Issue issue = new Issue();

        issue.setId(123);
        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.HIGH);
        issue.setSubmitterId(users.get(0).getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(users.get(0).getId());
        issue0Assignees.add(users.get(1).getId());
        issue0Assignees.add(users.get(2).getId());
        issue.setAssigneeIds(issue0Assignees);
        issue.setCreatedAt(Instant.EPOCH.plusSeconds(1));

        return issue;
    }

    public static Issue issueUpdateModifiedIssue() {
        List<User> users = UserData.userList();
        Issue issue = new Issue();

        issue.setId(123);
        issue.setTitle("Modified Test Issue");
        issue.setDescription("This is a modified test issue.");
        issue.setStatus(Status.MORE_INFO_NEEDED);
        issue.setPriority(Priority.LOW);
        issue.setSubmitterId(users.get(0).getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(users.get(0).getId());
        issue0Assignees.add(users.get(1).getId());
        issue0Assignees.add(users.get(2).getId());
        issue.setAssigneeIds(issue0Assignees);
        issue.setCreatedAt(Instant.EPOCH.plusSeconds(1));

        return issue;
    }

    public static IssueDto issueUpdateModifiedIssueDto() {
        List<UserDto> userDtos = UserData.userDtoList();
        IssueDto dto = new IssueDto();

        dto.setId(123);
        dto.setTitle("Modified Test Issue");
        dto.setDescription("This is a modified test issue.");
        dto.setStatus(Status.MORE_INFO_NEEDED);
        dto.setPriority(Priority.LOW);
        dto.setSubmitter(userDtos.get(0));
        Set<UserDto> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(userDtos.get(0));
        issue0Assignees.add(userDtos.get(1));
        issue0Assignees.add(userDtos.get(2));
        dto.setAssignees(issue0Assignees);
        dto.setCreatedAt(Instant.EPOCH.plusSeconds(1).toString());

        return dto;
    }

}
