package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.model.PageMetadata;
import com.snetsrac.issuetracker.user.UserTestData;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class IssueMapperTest {

    @Test
    void withNullInput_toDto_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toDto(null, Set.copyOf(UserTestData.USER_DTO_LIST)));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toDto(IssueTestData.ISSUE, null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toDto(null, null));
    }

    @Test
    void withValidInput_toDto_ReturnsIssueDto() {
        assertThat(IssueMapper.toDto(IssueTestData.ISSUE, Set.copyOf(UserTestData.USER_DTO_LIST)))
                .isEqualTo(IssueTestData.ISSUE_DTO);
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
                .isThrownBy(() -> IssueMapper.toPageDto(null, Set.copyOf(UserTestData.USER_DTO_LIST)));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toPageDto(new PageImpl<>(IssueTestData.ISSUE_LIST), null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toPageDto(null, null));
    }

    @Test
    void withValidInput_toPageDto_ReturnsPageDto() {
        Page<Issue> page = new PageImpl<>(IssueTestData.ISSUE_LIST);
        PageDto<IssueDto> pageDto = new PageDto<>();
        pageDto.setContent(IssueTestData.ISSUE_DTO_LIST);
        pageDto.setPageMetadata(new PageMetadata(page));

        assertThat(IssueMapper.toPageDto(page, Set.copyOf(UserTestData.USER_DTO_LIST))).isEqualTo(pageDto);
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
                .isThrownBy(() -> IssueMapper.toIssue(null, IssueTestData.ISSUE_CREATION_ISSUE.getSubmitterId()));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(IssueTestData.ISSUE_CREATION_DTO, null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(IssueTestData.ISSUE_CREATION_DTO, ""));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toIssue(null, null));
    }

    @Test
    void withValidInput_toIssue_ReturnsIssue() {
        assertThat(IssueMapper.toIssue(IssueTestData.ISSUE_CREATION_DTO,
                IssueTestData.ISSUE_CREATION_ISSUE.getSubmitterId()))
                .isEqualTo(IssueTestData.ISSUE_CREATION_ISSUE);
    }

    @Test
    void withInvalidInput_ontoIssue_Throws() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(null, IssueTestData.ISSUE_UPDATE_ISSUE));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(IssueTestData.ISSUE_UPDATE_DTO, null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.ontoIssue(null, null));
    }

    @Test
    void withValidInput_ontoIssue_ReturnsIssue() {
        assertThat(IssueMapper.ontoIssue(IssueTestData.ISSUE_UPDATE_DTO, IssueTestData.ISSUE_UPDATE_ISSUE))
                .isEqualTo(IssueTestData.ISSUE_UPDATE_MODIFIED_ISSUE);
    }
}
