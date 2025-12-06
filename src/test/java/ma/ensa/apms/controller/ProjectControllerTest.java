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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.service.ProjectService;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID projectId;
    private UUID productBacklogId;
    private ProjectResponse projectResponse;
    private List<ProjectResponse> projectResponseList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        productBacklogId = UUID.randomUUID();
        startDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        endDate = LocalDateTime.of(2025, 12, 31, 18, 0);

        projectResponse = ProjectResponse.builder()
                .id(projectId)
                .name("Test Project")
                .description("Test Description")
                .status(ProjectStatus.NOT_STARTED)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        projectResponseList = Arrays.asList(projectResponse);
    }

    @Test
    void getProject_ShouldReturnProject() throws Exception {
        when(projectService.getProject(projectId)).thenReturn(projectResponse);

        mockMvc.perform(get("/api/v1/projects/{id}", projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()))
                .andExpect(jsonPath("$.name").value(projectResponse.getName()));

        verify(projectService).getProject(projectId);
    }

    @Test
    void getAllProjects_ShouldReturnAllProjects() throws Exception {
        when(projectService.getAllProjects(any(Pageable.class))).thenReturn(projectResponseList);

        mockMvc.perform(get("/api/v1/projects")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(projectId.toString()));

        verify(projectService).getAllProjects(any(Pageable.class));
    }

    @Test
    void getProjectsByStatus_ShouldReturnProjectsWithSpecifiedStatus() throws Exception {
        when(projectService.getProjectsByStatus(any(ProjectStatus.class))).thenReturn(projectResponseList);

        mockMvc.perform(get("/api/v1/projects/status/{status}", ProjectStatus.IN_PROGRESS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("NOT_STARTED"));

        verify(projectService).getProjectsByStatus(ProjectStatus.IN_PROGRESS);
    }

    @Test
    void getProjectsBetweenDates_ShouldReturnProjectsInSpecifiedRange() throws Exception {
        when(projectService.getProjectsBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(projectResponseList);

        mockMvc.perform(get("/api/v1/projects/range")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(projectService).getProjectsBetweenDates(any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void updateProjectStartDate_ShouldReturnUpdatedProject() throws Exception {
        LocalDateTime newStartDate = LocalDateTime.now().plusDays(1);
        when(projectService.updateProjectStartDate(eq(projectId), any(LocalDateTime.class)))
                .thenReturn(projectResponse);

        mockMvc.perform(patch("/api/v1/projects/{id}/startDate", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStartDate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()));

        verify(projectService).updateProjectStartDate(eq(projectId), any(LocalDateTime.class));
    }

    @Test
    void updateProjectEndDate_ShouldReturnUpdatedProject() throws Exception {
        LocalDateTime newEndDate = LocalDateTime.now().plusMonths(6);
        when(projectService.updateProjectEndDate(eq(projectId), any(LocalDateTime.class)))
                .thenReturn(projectResponse);

        mockMvc.perform(patch("/api/v1/projects/{id}/endDate", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEndDate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()));

        verify(projectService).updateProjectEndDate(eq(projectId), any(LocalDateTime.class));
    }

    @Test
    void updateProjectStatus_ShouldReturnUpdatedProject() throws Exception {
        ProjectStatus newStatus = ProjectStatus.IN_PROGRESS;
        when(projectService.updateProjectStatus(eq(projectId), any(ProjectStatus.class)))
                .thenReturn(projectResponse);

        mockMvc.perform(patch("/api/v1/projects/{id}/status", projectId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()));

        verify(projectService).updateProjectStatus(eq(projectId), any(ProjectStatus.class));
    }

    @Test
    void deleteProject_ShouldReturnNoContent() throws Exception {
        doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(delete("/api/v1/projects/{id}", projectId))
                .andExpect(status().isOk());

        verify(projectService).deleteProject(projectId);
    }

    @Test
    void assignProductBacklogToProject_ShouldReturnUpdatedProject() throws Exception {
        when(projectService.assignProductBacklogToProject(projectId, productBacklogId))
                .thenReturn(projectResponse);

        mockMvc.perform(patch("/api/v1/projects/{id}/assign-product-backlog/{productBacklogId}",
                projectId, productBacklogId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()));

        verify(projectService).assignProductBacklogToProject(projectId, productBacklogId);
    }
}
