package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snetsrac.issuetracker.issue.Issue.Priority;
import com.snetsrac.issuetracker.issue.Issue.Status;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class IssueMapperTest {
    
    private static Issue issue;
    private static Map<String, UserDto> userMap;
    private static Page<Issue> issuePage;

    private IssueMapper issueMapper;

    @BeforeAll
    static void initData() {
        issue = new Issue();
        issue.setId(123);
        issue.setTitle("Test Issue");
        issue.setDescription("This is a test issue.");
        issue.setStatus(Status.OPEN);
        issue.setPriority(Priority.HIGH);
        issue.setSubmitterId("0");
        issue.setAssigneeIds(Set.of("1", "2"));
        issue.setCreatedAt(Instant.EPOCH);

        userMap = Map.of("0", new UserDto(), "1", new UserDto(), "2", new UserDto());
        userMap.get("0").setId("0");
        userMap.get("1").setId("1");
        userMap.get("2").setId("2");

        issuePage = new PageImpl<>(List.of(issue));
    }

    @BeforeEach
    void init() {
        issueMapper = new IssueMapper();
    }

    @Test
    void toDtoThrowsIfArgumentsAreNull() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> issueMapper.toDto(null, userMap));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> issueMapper.toDto(issue, null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> issueMapper.toDto(null, null));
    }
    
    @Test
    void toDtoReturnsIssueDto() {
        IssueDto issueDto = new IssueDto();
        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(userMap.get("0"));
        issueDto.setAssignees(Set.of(userMap.get("1"), userMap.get("2")));
        issueDto.setCreatedAt(Instant.EPOCH.toString());

        assertThat(issueMapper.toDto(issue, userMap)).isEqualTo(issueDto);
    }

    @Test
    void toDtoReturnsIssueDtoIfSubmitterNotInMap() {
        Map<String, UserDto> partialUserMap = Map.of("1", new UserDto(), "2", new UserDto());
        partialUserMap.get("1").setId("1");
        partialUserMap.get("2").setId("2");

        IssueDto issueDto = new IssueDto();
        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(null);
        issueDto.setAssignees(Set.of(userMap.get("1"), userMap.get("2")));
        issueDto.setCreatedAt(Instant.EPOCH.toString());

        assertThat(issueMapper.toDto(issue, partialUserMap)).isEqualTo(issueDto);
    }

    @Test
    void toDtoReturnsIssueDtoIfAssigneeNotInMap() {
        Map<String, UserDto> partialUserMap = Map.of("2", new UserDto());
        partialUserMap.get("2").setId("2");

        IssueDto issueDto = new IssueDto();
        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(null);
        issueDto.setAssignees(Set.of(userMap.get("2")));
        issueDto.setCreatedAt(Instant.EPOCH.toString());

        assertThat(issueMapper.toDto(issue, partialUserMap)).isEqualTo(issueDto);
    }

    @Test
    void toDtoReturnsIssueDtoIfAllAssigneesNotInMap() {
        IssueDto issueDto = new IssueDto();
        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(null);
        issueDto.setAssignees(Set.of());
        issueDto.setCreatedAt(Instant.EPOCH.toString());

        assertThat(issueMapper.toDto(issue, Map.of())).isEqualTo(issueDto);
    }

    @Test
    void toPageDtoThrowsIfArgumentsAreNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> issueMapper.toPageDto(null, userMap));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> issueMapper.toPageDto(issuePage, null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> issueMapper.toPageDto(null, null));
    }
    
    @Test
    void toPageDtoReturnsPageDto() {
        IssueDto issueDto = new IssueDto();
        issueDto.setId(123);
        issueDto.setTitle("Test Issue");
        issueDto.setDescription("This is a test issue.");
        issueDto.setStatus(Status.OPEN);
        issueDto.setPriority(Priority.HIGH);
        issueDto.setSubmitter(userMap.get("0"));
        issueDto.setAssignees(Set.of(userMap.get("1"), userMap.get("2")));
        issueDto.setCreatedAt(Instant.EPOCH.toString());

        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(List.of(issueDto));
        pageDto.setPageMetadata(new PageMetadata(issuePage));

        assertThat(issueMapper.toPageDto(issuePage, userMap)).isEqualTo(pageDto);
    }
    
    @Test
    void toPageDtoReturnsEmptyPageDto() {
        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(List.of());
        pageDto.setPageMetadata(new PageMetadata(Page.empty()));

        assertThat(issueMapper.toPageDto(Page.empty(), userMap)).isEqualTo(pageDto);
    }

}
