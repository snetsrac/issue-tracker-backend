package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class IssueServiceImplTest {

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueServiceImpl issueService;

    @Test
    void findAllReturnsEmptyPage() {
        Pageable pageable = Pageable.unpaged();
        Page<Issue> page = Page.empty(pageable);

        when(issueRepository.findAll(pageable)).thenReturn(page);

        assertThat(issueService.findAll(pageable)).isNotNull().isSameAs(page);
    }

    @Test
    void findAllReturnsPage() {
        Pageable pageable = Pageable.unpaged();
        Page<Issue> page = new PageImpl<>(IssueTestData.ISSUE_LIST, pageable, IssueTestData.ISSUE_LIST.size());

        when(issueRepository.findAll(pageable)).thenReturn(page);

        assertThat(issueService.findAll(pageable)).isNotNull().isSameAs(page);
    }

    @Test
    void saveThrowsGivenNull() {
        when(issueRepository.save(null)).thenThrow(IllegalArgumentException.class);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> issueService.save(null));
    }

    @Test
    void saveReturnsUpdatedIssue() {
        when(issueRepository.save(IssueTestData.ISSUE_LIST.get(0))).thenReturn(IssueTestData.ISSUE_LIST.get(1));

        assertThat(issueService.save(IssueTestData.ISSUE_LIST.get(0))).isSameAs(IssueTestData.ISSUE_LIST.get(1));
    }

    @Test
    void findByIdReturnsEmptyIfNotFound() {
        when(issueRepository.findById(3)).thenReturn(Optional.empty());

        assertThat(issueService.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void findByIdReturnsIssue() {
        when(issueRepository.findById(0)).thenReturn(Optional.of(IssueTestData.ISSUE));

        assertThat(issueService.findById(0)).isEqualTo(Optional.of(IssueTestData.ISSUE));
    }

    @Test
    void deleteByIdCallsRepository() {
        issueService.deleteById(0);

        verify(issueRepository).deleteById(0);
    }
}
