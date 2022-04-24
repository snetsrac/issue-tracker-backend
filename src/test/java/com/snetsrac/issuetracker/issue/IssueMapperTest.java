package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.UserData;
import com.snetsrac.issuetracker.user.dto.UserDto;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class IssueMapperTest {

    private static List<Issue> issues;
    private static List<IssueDto> issueDtos;
    private static List<UserDto> userDtos;
    private static IssueCreationDto issueCreationDto;
    private static Issue issueCreationIssue;
    private static IssueUpdateDto issueUpdateDto;
    private static Issue issueUpdateIssue;
    private static Issue issueUpdateModifiedIssue;

    @BeforeAll
    static void initData() {
        issues = IssueData.issueList();
        issueDtos = IssueData.issueDtoList();
        userDtos = UserData.userDtoList();
        issueCreationDto = IssueData.issueCreationDto();
        issueCreationIssue = IssueData.issueCreationIssue();
        issueUpdateDto = IssueData.issueUpdateDto();
        issueUpdateIssue = IssueData.issueUpdateIssue();
        issueUpdateModifiedIssue = IssueData.issueUpdateModifiedIssue();
    }

    @Test
    void withNullInput_toDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toDto(null, Set.copyOf(userDtos)));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toDto(issues.get(0), null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toDto(null, null));
    }

    @Test
    void withValidInput_toDto_ReturnsIssueDto() {
        assertThat(IssueMapper.toDto(issues.get(0), Set.copyOf(userDtos))).isEqualTo(issueDtos.get(0));
    }

    @Test
    void withUserNotInMap_toDto_ReturnsIssueDto() {
        Issue issue = new Issue();
        issue.setSubmitterId("123");
        issue.setAssigneeIds(Set.of("123", "456", "789"));

        IssueDto issueDto = new IssueDto();
        issueDto.setSubmitter(null);
        issueDto.setAssignees(Set.of());

        assertThat(IssueMapper.toDto(issue, Set.of())).isEqualTo(issueDto);
    }

    @Test
    void withNullInput_toPageDto_Throws() {

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toPageDto(null, Set.copyOf(userDtos)));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toPageDto(new PageImpl<>(issues), null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toPageDto(null, null));
    }

    @Test
    void withValidInput_toPageDto_ReturnsPageDto() {
        Page<Issue> page = new PageImpl<>(issues);
        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(issueDtos);
        pageDto.setPageMetadata(new PageMetadata(page));

        assertThat(IssueMapper.toPageDto(page, Set.copyOf(userDtos))).isEqualTo(pageDto);
    }

    @Test
    void withEmptyInput_toPageDto_ReturnsEmptyPageDto() {
        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(List.of());
        pageDto.setPageMetadata(new PageMetadata(Page.empty()));

        assertThat(IssueMapper.toPageDto(Page.empty(), Set.of())).isEqualTo(pageDto);
    }

    @Test
    void withInvalidInput_toIssue_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(null, issueCreationIssue.getSubmitterId()));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(issueCreationDto, null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(issueCreationDto, ""));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(null, null));
    }

    @Test
    void withValidInput_toIssue_ReturnsIssue() {
        assertThat(IssueMapper.toIssue(issueCreationDto, issueCreationIssue.getSubmitterId()))
                .isEqualTo(issueCreationIssue);
    }

    @Test
    void withInvalidInput_ontoIssue_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(null, issueUpdateIssue));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(issueUpdateDto, null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(null, null));
    }

    @Test
    void withValidInput_ontoIssue_ReturnsIssue() {
        assertThat(IssueMapper.ontoIssue(issueUpdateDto, issueUpdateIssue))
                .isEqualTo(issueUpdateModifiedIssue);
    }
}
