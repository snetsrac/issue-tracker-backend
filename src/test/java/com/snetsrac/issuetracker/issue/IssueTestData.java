package com.snetsrac.issuetracker.issue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.UserTestData;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class IssueTestData {

    public static final Issue ISSUE;
    public static final IssueDto ISSUE_DTO;
    public static final List<Issue> ISSUE_LIST;
    public static final List<IssueDto> ISSUE_DTO_LIST;
    public static final Page<Issue> ISSUE_PAGE;
    public static final PageDto<IssueDto> ISSUE_DTO_PAGE_DTO;
    public static final IssueCreationDto ISSUE_CREATION_DTO;
    public static final Issue ISSUE_CREATION_ISSUE;
    public static final IssueUpdateDto ISSUE_UPDATE_DTO;
    public static final Issue ISSUE_UPDATE_ISSUE;
    public static final Issue ISSUE_UPDATE_MODIFIED_ISSUE;
    public static final IssueDto ISSUE_UPDATE_MODIFIED_ISSUE_DTO;

    static {
        ISSUE = issue();
        ISSUE_DTO = issueDto();
        ISSUE_LIST = issueList();
        ISSUE_DTO_LIST = issueDtoList();
        ISSUE_PAGE = issuePage();
        ISSUE_DTO_PAGE_DTO = issueDtoPageDto();
        ISSUE_CREATION_DTO = issueCreationDto();
        ISSUE_CREATION_ISSUE = issueCreationIssue();
        ISSUE_UPDATE_DTO = issueUpdateDto();
        ISSUE_UPDATE_ISSUE = issueUpdateIssue();
        ISSUE_UPDATE_MODIFIED_ISSUE = issueUpdateModifiedIssue();
        ISSUE_UPDATE_MODIFIED_ISSUE_DTO = issueUpdateModifiedIssueDto();
    }

    // Prevent initialization
    private IssueTestData() {
    }

    private static Issue issue() {
        Issue issue = new Issue();

        issue.setId(123);
        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.HIGH);
        issue.setSubmitterId(UserTestData.USER.getId());
        Set<String> issueAssignees = new LinkedHashSet<>();
        issueAssignees.add(UserTestData.USER_LIST.get(0).getId());
        issueAssignees.add(UserTestData.USER_LIST.get(1).getId());
        issueAssignees.add(UserTestData.USER_LIST.get(2).getId());
        issue.setAssigneeIds(issueAssignees);
        issue.setCreatedAt(Instant.EPOCH.plusSeconds(1));

        return issue;
    }

    private static IssueDto issueDto() {
        IssueDto issueDto = new IssueDto();

        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(UserTestData.USER_DTO_LIST.get(0));
        Set<UserDto> issueAssignees = new LinkedHashSet<>();
        issueAssignees.add(UserTestData.USER_DTO_LIST.get(0));
        issueAssignees.add(UserTestData.USER_DTO_LIST.get(1));
        issueAssignees.add(UserTestData.USER_DTO_LIST.get(2));
        issueDto.setAssignees(issueAssignees);
        issueDto.setCreatedAt(Instant.EPOCH.plusSeconds(1).toString());

        return issueDto;
    }

    private static List<Issue> issueList() {
        List<Issue> issues = new ArrayList<>();
        issues.add(new Issue());
        issues.add(new Issue());
        issues.add(new Issue());

        issues.get(0).setId(123);
        issues.get(0).setTitle("Test Issue 1");
        issues.get(0).setDescription("This is the first test issue.");
        issues.get(0).setStatus(Status.OPEN);
        issues.get(0).setPriority(Priority.HIGH);
        issues.get(0).setSubmitterId(UserTestData.USER.getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(UserTestData.USER_LIST.get(0).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(1).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(2).getId());
        issues.get(0).setAssigneeIds(issue0Assignees);
        issues.get(0).setCreatedAt(Instant.EPOCH.plusSeconds(1));

        issues.get(1).setId(456);
        issues.get(1).setTitle("Test Issue 2");
        issues.get(1).setDescription("This is the second test issue.");
        issues.get(1).setStatus(Status.OPEN);
        issues.get(1).setPriority(Priority.HIGH);
        issues.get(1).setSubmitterId(null);
        Set<String> issue1Assignees = new LinkedHashSet<>();
        issue1Assignees.add(UserTestData.USER_LIST.get(1).getId());
        issue1Assignees.add(UserTestData.USER_LIST.get(2).getId());
        issues.get(1).setAssigneeIds(issue1Assignees);
        issues.get(1).setCreatedAt(Instant.EPOCH.plusSeconds(2));

        issues.get(2).setId(789);
        issues.get(2).setTitle("Test Issue 3");
        issues.get(2).setDescription("This is the third test issue.");
        issues.get(2).setStatus(Status.OPEN);
        issues.get(2).setPriority(Priority.HIGH);
        issues.get(2).setSubmitterId(UserTestData.USER.getId());
        issues.get(2).setAssigneeIds(new LinkedHashSet<>());
        issues.get(2).setCreatedAt(Instant.EPOCH.plusSeconds(3));

        return issues;
    }

    private static List<IssueDto> issueDtoList() {
        List<IssueDto> issueDtos = new ArrayList<>();
        issueDtos.add(new IssueDto());
        issueDtos.add(new IssueDto());
        issueDtos.add(new IssueDto());

        issueDtos.get(0).setId(123);
        issueDtos.get(0).setTitle("Test Issue 1");
        issueDtos.get(0).setDescription("This is the first test issue.");
        issueDtos.get(0).setStatus(Status.OPEN);
        issueDtos.get(0).setPriority(Priority.HIGH);
        issueDtos.get(0).setSubmitter(UserTestData.USER_DTO_LIST.get(0));
        Set<UserDto> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(0));
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(1));
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(2));
        issueDtos.get(0).setAssignees(issue0Assignees);
        issueDtos.get(0).setCreatedAt(Instant.EPOCH.plusSeconds(1).toString());

        issueDtos.get(1).setId(456);
        issueDtos.get(1).setTitle("Test Issue 2");
        issueDtos.get(1).setDescription("This is the second test issue.");
        issueDtos.get(1).setStatus(Status.OPEN);
        issueDtos.get(1).setPriority(Priority.HIGH);
        issueDtos.get(1).setSubmitter(null);
        Set<UserDto> issue1Assignees = new LinkedHashSet<>();
        issue1Assignees.add(UserTestData.USER_DTO_LIST.get(1));
        issue1Assignees.add(UserTestData.USER_DTO_LIST.get(2));
        issueDtos.get(1).setAssignees(issue1Assignees);
        issueDtos.get(1).setCreatedAt(Instant.EPOCH.plusSeconds(2).toString());

        issueDtos.get(2).setId(789);
        issueDtos.get(2).setTitle("Test Issue 3");
        issueDtos.get(2).setDescription("This is the third test issue.");
        issueDtos.get(2).setStatus(Status.OPEN);
        issueDtos.get(2).setPriority(Priority.HIGH);
        issueDtos.get(2).setSubmitter(UserTestData.USER_DTO_LIST.get(0));
        issueDtos.get(2).setAssignees(Set.of());
        issueDtos.get(2).setCreatedAt(Instant.EPOCH.plusSeconds(3).toString());

        return issueDtos;
    }

    private static Page<Issue> issuePage() {
        return new PageImpl<>(issueList());
    }

    private static PageDto<IssueDto> issueDtoPageDto() {
        PageDto<IssueDto> pageDto = new PageDto<>();

        pageDto.setContent(issueDtoList());
        pageDto.setPageMetadata(new PageMetadata(issuePage()));

        return pageDto;
    }

    private static IssueCreationDto issueCreationDto() {
        IssueCreationDto dto = new IssueCreationDto();

        dto.setTitle("Test Issue");
        dto.setDescription("This is a test issue.");
        dto.setPriority(Priority.MEDIUM.toString());

        return dto;
    }

    private static Issue issueCreationIssue() {
        Issue issue = new Issue();

        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.MEDIUM);
        issue.setSubmitterId("123");

        return issue;
    }

    private static IssueUpdateDto issueUpdateDto() {
        IssueUpdateDto dto = new IssueUpdateDto();

        dto.setTitle("Modified Test Issue");
        dto.setDescription("This is a modified test issue.");
        dto.setStatus(Status.MORE_INFO_NEEDED.toString());
        dto.setPriority(Priority.LOW.toString());

        return dto;
    }

    private static Issue issueUpdateIssue() {
        Issue issue = new Issue();

        issue.setId(123);
        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.HIGH);
        issue.setSubmitterId(UserTestData.USER.getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(UserTestData.USER_LIST.get(0).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(1).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(2).getId());
        issue.setAssigneeIds(issue0Assignees);
        issue.setCreatedAt(Instant.EPOCH.plusSeconds(1));

        return issue;
    }

    private static Issue issueUpdateModifiedIssue() {
        Issue issue = new Issue();

        issue.setId(123);
        issue.setTitle("Modified Test Issue");
        issue.setDescription("This is a modified test issue.");
        issue.setStatus(Status.MORE_INFO_NEEDED);
        issue.setPriority(Priority.LOW);
        issue.setSubmitterId(UserTestData.USER.getId());
        Set<String> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(UserTestData.USER_LIST.get(0).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(1).getId());
        issue0Assignees.add(UserTestData.USER_LIST.get(2).getId());
        issue.setAssigneeIds(issue0Assignees);
        issue.setCreatedAt(Instant.EPOCH.plusSeconds(1));

        return issue;
    }

    private static IssueDto issueUpdateModifiedIssueDto() {
        IssueDto dto = new IssueDto();

        dto.setId(123);
        dto.setTitle("Modified Test Issue");
        dto.setDescription("This is a modified test issue.");
        dto.setStatus(Status.MORE_INFO_NEEDED);
        dto.setPriority(Priority.LOW);
        dto.setSubmitter(UserTestData.USER_DTO_LIST.get(0));
        Set<UserDto> issue0Assignees = new LinkedHashSet<>();
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(0));
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(1));
        issue0Assignees.add(UserTestData.USER_DTO_LIST.get(2));
        dto.setAssignees(issue0Assignees);
        dto.setCreatedAt(Instant.EPOCH.plusSeconds(1).toString());

        return dto;
    }

}
