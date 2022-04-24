package com.snetsrac.issuetracker.issue;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.json.mgmt.users.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.snetsrac.issuetracker.issue.dto.IssueCreationDto;
import com.snetsrac.issuetracker.issue.dto.IssueDto;
import com.snetsrac.issuetracker.issue.dto.IssueUpdateDto;
import com.snetsrac.issuetracker.model.PageDto;
import com.snetsrac.issuetracker.user.UserData;
import com.snetsrac.issuetracker.user.UserService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = IssueController.class)
public class IssueControllerTest {

    private static List<Issue> issueList;
    private static List<IssueDto> issueDtoList;
    private static Page<Issue> issuePage;
    private static PageDto<IssueDto> issuePageDto;
    private static List<User> userList;
    private static IssueCreationDto issueCreationDto;
    private static Issue issueCreationIssue;
    private static IssueUpdateDto issueUpdateDto;
    private static Issue issueUpdateIssue;
    private static Issue issueUpdateModifiedIssue;
    private static IssueDto issueUpdateModifiedIssueDto;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @MockBean
    private ManagementAPI auth0ManagementApi;

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeAll
    static void initData() {
        issueList = IssueData.issueList();
        issueDtoList = IssueData.issueDtoList();
        issuePage = IssueData.issuePage();
        issuePageDto = IssueData.issuePageDto();
        userList = UserData.userList();
        issueCreationDto = IssueData.issueCreationDto();
        issueCreationIssue = IssueData.issueCreationIssue();
        issueUpdateDto = IssueData.issueUpdateDto();
        issueUpdateIssue = IssueData.issueUpdateIssue();
        issueUpdateModifiedIssue = IssueData.issueUpdateModifiedIssue();
        issueUpdateModifiedIssueDto = IssueData.issueUpdateModifiedIssueDto();
    }

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    // GET /issues

    @Test
    void withoutAuth_GetIssues_Returns401() throws Exception {
        mockMvc.perform(get(IssueController.ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void withoutRole_GetIssues_Returns403() throws Exception {
        mockMvc.perform(get(IssueController.ENDPOINT))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "read:issues")
    void withInvalidInput_GetIssues_Returns200() throws Exception {
        Pageable pageable = PageRequest.of(0, 20, Sort.by("aoeuiao"));
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));
        when(issueService.findAll(any(Pageable.class))).thenReturn(issuePage);

        MvcResult mvcResult = mockMvc.perform(get(IssueController.ENDPOINT)
                .param("page", "three")
                .param("size", "eighteen")
                .param("sort", "aoeuiao"))
                .andExpect(status().isOk())
                .andReturn();

        verify(issueService).findAll(pageable);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issuePageDto));
    }

    @Test
    @WithMockUser(authorities = "read:issues")
    void withValidInput_GetIssues_Returns200() throws Exception {
        Pageable pageable = PageRequest.of(3, 18, Sort.by(Order.desc("id")));
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));
        when(issueService.findAll(any(Pageable.class))).thenReturn(issuePage);

        MvcResult mvcResult = mockMvc.perform(get(IssueController.ENDPOINT)
                .param("page", "3")
                .param("size", "18")
                .param("sort", "id,desc"))
                .andExpect(status().isOk())
                .andReturn();

        verify(issueService).findAll(pageable);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issuePageDto));
    }

    // POST /issues

    @Test
    void withoutAuth_PostIssue_Returns401() throws Exception {
        mockMvc.perform(post(IssueController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueCreationDto)))
                .andExpect(status().isUnauthorized());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser
    void withoutRole_PostIssue_Returns403() throws Exception {
        mockMvc.perform(post(IssueController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueCreationDto)))
                .andExpect(status().isForbidden());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser(username = "123", authorities = "submit:issues")
    void withInvalidInput_PostIssue_Returns400() throws Exception {
        mockMvc.perform(post(IssueController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new IssueCreationDto())))
                .andExpect(status().isBadRequest());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser(username = "123", authorities = "submit:issues")
    void withValidInput_PostIssue_Returns200() throws Exception {
        when(issueService.save(any(Issue.class))).thenReturn(issueList.get(0));
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));

        MvcResult mvcResult = mockMvc.perform(post(IssueController.ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueCreationDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", IssueController.ENDPOINT + "/" + issueList.get(0).getId()))
                .andReturn();

        verify(issueService).save(issueCreationIssue);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issueDtoList.get(0)));
    }

    // GET /issues/:id

    @Test
    void withoutAuth_GetIssue_Returns401() throws Exception {
        mockMvc.perform(get(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void withoutRole_GetIssue_Returns403() throws Exception {
        mockMvc.perform(get(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "read:issues")
    void withNonexistentIssue_GetIssue_Returns404() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(get(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isNotFound());

        verify(issueService).findById(123);
    }

    @Test
    @WithMockUser(authorities = "read:issues")
    void withValidInput_GetIssue_Returns200() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.of(issueList.get(0)));
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));

        MvcResult mvcResult = mockMvc.perform(get(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isOk())
                .andReturn();

        verify(issueService).findById(123);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issueDtoList.get(0)));
    }

    // PUT /issues/:id

    @Test
    void withoutAuth_PutIssue_Returns401() throws Exception {
        mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueUpdateDto)))
                .andExpect(status().isUnauthorized());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser
    void withoutRole_PutIssue_Returns403() throws Exception {
        mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueUpdateDto)))
                .andExpect(status().isForbidden());

        verify(issueService, times(0)).save(any());

    }

    @Test
    @WithMockUser(authorities = "submit:issues")
    void withoutOwnership_PutIssue_Returns403() throws Exception {
        mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueUpdateDto)))
                .andExpect(status().isForbidden());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser(authorities = "modify:issues")
    void withInvalidInput_PutIssue_Returns400() throws Exception {
        mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString("{\"title\": true}")))
                .andExpect(status().isBadRequest());

        verify(issueService, times(0)).save(any());
    }

    @Test
    @WithMockUser(authorities = "modify:issues")
    void withValidInputAndModifyScope_PutIssue_Returns200() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.of(issueUpdateIssue));
        when(issueService.save(any(Issue.class))).thenReturn(issueUpdateModifiedIssue);
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));

        MvcResult mvcResult = mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        verify(issueService).save(issueUpdateIssue);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issueUpdateModifiedIssueDto));
    }

    @Test
    @WithMockUser(username = "123", authorities = "submit:issues")
    void withValidInputAndSubmitScope_PutIssue_Returns200() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.of(issueUpdateIssue));
        when(issueService.save(any(Issue.class))).thenReturn(issueUpdateModifiedIssue);
        when(userService.findByIds(anyCollection())).thenReturn(Set.copyOf(userList));

        MvcResult mvcResult = mockMvc.perform(put(IssueController.ENDPOINT + "/123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(issueUpdateDto)))
                .andExpect(status().isOk())
                .andReturn();

        verify(issueService).save(issueUpdateIssue);
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(issueUpdateModifiedIssueDto));
    }

    // DELETE /issues/:id

    @Test
    void withoutAuth_DeleteIssue_Returns401() throws Exception {
        mockMvc.perform(delete(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void withoutRole_DeleteIssue_Returns403() throws Exception {
        mockMvc.perform(delete(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "delete:issues")
    void withNonexistentIssue_DeleteIssue_Returns404() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(delete(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isNotFound());

        verify(issueService, times(0)).deleteById(anyInt());
        ;
    }

    @Test
    @WithMockUser(authorities = "delete:issues")
    void withValidInput_DeleteIssue_Returns200() throws Exception {
        when(issueService.findById(anyInt())).thenReturn(Optional.of(issueList.get(0)));

        mockMvc.perform(delete(IssueController.ENDPOINT + "/123"))
                .andExpect(status().isNoContent());

        verify(issueService).deleteById(123);
    }

}
