package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
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

    private static List<Issue> issueList;

    @BeforeAll
    static void initData() {
        issueList = List.of(new Issue(), new Issue(), new Issue());
    }

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
        Page<Issue> page = new PageImpl<>(issueList, pageable, issueList.size());

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
        when(issueRepository.save(issueList.get(0))).thenReturn(issueList.get(1));

        assertThat(issueService.save(issueList.get(0))).isSameAs(issueList.get(1));
    }

    @Test
    void findByIdReturnsEmptyIfNotFound() {
        when(issueRepository.findById(3)).thenReturn(Optional.empty());

        assertThat(issueService.findById(3)).isEqualTo(Optional.empty());
    }

    @Test
    void findByIdReturnsIssue() {
        when(issueRepository.findById(0)).thenReturn(Optional.of(issueList.get(0)));

        assertThat(issueService.findById(0)).isEqualTo(Optional.of(issueList.get(0)));
    }

    @Test
    void deleteByIdCallsRepository() {
        issueService.deleteById(0);

        verify(issueRepository).deleteById(0);
    }
}
