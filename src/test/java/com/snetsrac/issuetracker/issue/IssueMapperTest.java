package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueMapper;
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

    @BeforeAll
    static void initData() {
        issues = IssueData.issueList();
        issueDtos = IssueData.issueDtoList();
        userDtos = UserData.userDtoList();
    }

    @Test
    void withNullInput_toDto_Throws() {
        Map<String, UserDto> userMap = userDtos.stream().collect(Collectors.toMap(UserDto::getId, Function.identity()));

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toDto(null, userMap));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> IssueMapper.toDto(issues.get(0), null));
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> IssueMapper.toDto(null, null));
    }

    @Test
    void withValidInput_toDto_ReturnsIssueDto() {
        Map<String, UserDto> userMap = userDtos.stream().collect(Collectors.toMap(UserDto::getId, Function.identity()));

        assertThat(IssueMapper.toDto(issues.get(0), userMap)).isEqualTo(issueDtos.get(0));
    }

    @Test
    void withUserNotInMap_toDto_ReturnsIssueDto() {
        Issue issue = new Issue();
        issue.setSubmitterId("123");
        issue.setAssigneeIds(Set.of("123", "456", "789"));

        IssueDto issueDto = new IssueDto();
        issueDto.setSubmitter(null);
        issueDto.setAssignees(Set.of());

        assertThat(IssueMapper.toDto(issue, Map.of())).isEqualTo(issueDto);
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

}
