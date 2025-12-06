package ma.ensa.apms.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.service.UserStoryService;

@WebMvcTest(UserStoryController.class)
class UserStoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserStoryService userStoryService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID userStoryId;
    private UUID epicId;
    private UUID sprintId;
    private UserStoryRequest userStoryRequest;
    private UserStoryResponse userStoryResponse;
    private List<UserStoryResponse> userStoryResponseList;
    private List<AcceptanceCriteriaResponse> acceptanceCriteriaResponseList;
    private AcceptanceCriteriaResponse acceptanceCriteriaResponse;

    @BeforeEach
    void setUp() {
        userStoryId = UUID.randomUUID();
        epicId = UUID.randomUUID();
        sprintId = UUID.randomUUID();

        userStoryRequest = UserStoryRequest.builder()
                .name("Test User Story")
                .role("As a user")
                .feature("I want to test")
                .benefit("So that it works")
                .priority(1)
                .status(UserStoryStatus.TODO)
                .build();

        userStoryResponse = UserStoryResponse.builder()
                .id(userStoryId)
                .name("Test User Story")
                .role("As a user")
                .feature("I want to test")
                .benefit("So that it works")
                .priority(1)
                .status(UserStoryStatus.TODO)
                .build();

        userStoryResponseList = Arrays.asList(userStoryResponse);

        acceptanceCriteriaResponse = AcceptanceCriteriaResponse.builder()
                .id(UUID.randomUUID())
                .given("Given condition")
                .when("When action")
                .then("Then result")
                .met(false)
                .build();

        acceptanceCriteriaResponseList = Arrays.asList(acceptanceCriteriaResponse);
    }

    @Test
    void create_ShouldReturnCreatedUserStory() throws Exception {
        when(userStoryService.create(any(UserStoryRequest.class))).thenReturn(userStoryResponse);

        mockMvc.perform(post("/api/v1/user-stories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()))
                .andExpect(jsonPath("$.name").value(userStoryRequest.getName()))
                .andExpect(jsonPath("$.role").value(userStoryRequest.getRole()));

        verify(userStoryService).create(any(UserStoryRequest.class));
    }

    @Test
    void getUserStoryById_ShouldReturnUserStory() throws Exception {
        when(userStoryService.getUserStoryById(userStoryId)).thenReturn(userStoryResponse);

        mockMvc.perform(get("/api/v1/user-stories/{id}", userStoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()))
                .andExpect(jsonPath("$.name").value(userStoryResponse.getName()));

        verify(userStoryService).getUserStoryById(userStoryId);
    }

    @Test
    void getAcceptanceCriteriasByUserStoryId_ShouldReturnList() throws Exception {
        when(userStoryService.getAcceptanceCriteriasByUserStoryId(userStoryId))
                .thenReturn(acceptanceCriteriaResponseList);

        mockMvc.perform(get("/api/v1/user-stories/{id}/acceptance-criterias", userStoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].given").value("Given condition"));

        verify(userStoryService).getAcceptanceCriteriasByUserStoryId(userStoryId);
    }

    @Test
    void updateUserStory_ShouldReturnUpdatedUserStory() throws Exception {
        when(userStoryService.updateUserStory(eq(userStoryId), any(UserStoryRequest.class)))
                .thenReturn(userStoryResponse);

        mockMvc.perform(put("/api/v1/user-stories/{id}", userStoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userStoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()));

        verify(userStoryService).updateUserStory(eq(userStoryId), any(UserStoryRequest.class));
    }

    @Test
    void linkToEpic_ShouldReturnUpdatedUserStory() throws Exception {
        when(userStoryService.linkToEpic(userStoryId, epicId)).thenReturn(userStoryResponse);

        mockMvc.perform(put("/api/v1/user-stories/{id}/link-to-epic/{epicId}", userStoryId, epicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()));

        verify(userStoryService).linkToEpic(userStoryId, epicId);
    }

    @Test
    void moveToSprint_ShouldReturnUpdatedUserStory() throws Exception {
        when(userStoryService.moveToSprint(userStoryId, sprintId)).thenReturn(userStoryResponse);

        mockMvc.perform(put("/api/v1/user-stories/{id}/move-to-sprint/{sprintId}", userStoryId, sprintId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()));

        verify(userStoryService).moveToSprint(userStoryId, sprintId);
    }

    @Test
    void changeStatus_ShouldReturnUpdatedUserStory() throws Exception {
        UserStoryStatus newStatus = UserStoryStatus.IN_PROGRESS;
        when(userStoryService.changeStatus(userStoryId, newStatus)).thenReturn(userStoryResponse);

        mockMvc.perform(patch("/api/v1/user-stories/{id}/change-status", userStoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userStoryId.toString()));

        verify(userStoryService).changeStatus(userStoryId, newStatus);
    }

    @Test
    void getUserStoriesByEpicId_ShouldReturnList() throws Exception {
        when(userStoryService.getUserStoriesByEpicId(epicId)).thenReturn(userStoryResponseList);

        mockMvc.perform(get("/api/v1/user-stories/epic/{epicId}", epicId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(userStoryId.toString()));

        verify(userStoryService).getUserStoriesByEpicId(epicId);
    }

    @Test
    void getUserStoriesBySprintBacklogId_ShouldReturnList() throws Exception {
        when(userStoryService.getUserStoriesBySprintBacklogId(sprintId))
                .thenReturn(userStoryResponseList);

        mockMvc.perform(get("/api/v1/user-stories/sprint-backlog/{sprintId}", sprintId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(userStoryId.toString()));

        verify(userStoryService).getUserStoriesBySprintBacklogId(sprintId);
    }

    @Test
    void delete_ShouldReturnNoContent() throws Exception {
        doNothing().when(userStoryService).delete(userStoryId);

        mockMvc.perform(delete("/api/v1/user-stories/{id}", userStoryId))
                .andExpect(status().isNoContent());

        verify(userStoryService).delete(userStoryId);
    }
}
