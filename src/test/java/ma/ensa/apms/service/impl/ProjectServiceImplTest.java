package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.ProjectMapper;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private ma.ensa.apms.service.helper.ProjectRepositoryHelper projectRepositoryHelper;

    @Mock
    private ma.ensa.apms.service.validator.ProjectValidator projectValidator;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private UUID testId;
    private Project testProject;
    private ProjectRequest testRequest;
    private ProjectResponse testResponse;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();

        testProject = new Project();
        testProject.setId(testId);
        testProject.setName("Test Project");
        testProject.setStatus(ProjectStatus.IN_PROGRESS);
        testProject.setStartDate(LocalDateTime.now());
        testProject.setEndDate(LocalDateTime.now().plusMonths(6));

        testRequest = new ProjectRequest();

        testResponse = new ProjectResponse();
        testResponse.setId(testId);
        testResponse.setName("Test Project");
        testResponse.setStatus(ProjectStatus.IN_PROGRESS);
    }

    @Test
    void createProject_ShouldCreateProject() {
        // Arrange
        when(projectMapper.toEntity(testRequest)).thenReturn(testProject);
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.createProject(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(projectRepository, times(1)).save(testProject);
        verify(projectMapper, times(1)).toResponse(testProject);
    }

    @Test
    void updateProject_WhenProjectExists_ShouldUpdateProject() {
        // Arrange
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.updateProject(testId, testRequest);

        // Assert
        assertNotNull(result);
        verify(projectRepositoryHelper, times(1)).findByIdOrThrow(testId);
        verify(projectMapper, times(1)).updateEntityFromRequest(testRequest, testProject);
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProject_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.updateProject(testId, testRequest));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void deleteProject_ShouldDeleteProject() {
        // Act
        projectService.deleteProject(testId);

        // Assert
        verify(projectRepository, times(1)).deleteById(testId);
    }

    @Test
    void updateProjectStartDate_WithValidDate_ShouldUpdateStartDate() {
        // Arrange
        LocalDateTime newStartDate = LocalDateTime.now().minusDays(1);
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.updateProjectStartDate(testId, newStartDate);

        // Assert
        assertNotNull(result);
        assertEquals(newStartDate, testProject.getStartDate());
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProjectStartDate_WhenStartDateAfterEndDate_ShouldThrowException() {
        // Arrange
        LocalDateTime invalidStartDate = LocalDateTime.now().plusYears(1);
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        doThrow(new BusinessException("Start date must be before end date"))
                .when(projectValidator).validateStartDate(invalidStartDate, testProject.getEndDate());

        // Act & Assert
        assertThrows(BusinessException.class, () -> projectService.updateProjectStartDate(testId, invalidStartDate));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void updateProjectStartDate_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        LocalDateTime newStartDate = LocalDateTime.now();
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.updateProjectStartDate(testId, newStartDate));
    }

    @Test
    void updateProjectEndDate_WithValidDate_ShouldUpdateEndDate() {
        // Arrange
        LocalDateTime newEndDate = LocalDateTime.now().plusMonths(12);
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.updateProjectEndDate(testId, newEndDate);

        // Assert
        assertNotNull(result);
        assertEquals(newEndDate, testProject.getEndDate());
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProjectEndDate_WhenEndDateBeforeStartDate_ShouldThrowException() {
        // Arrange
        LocalDateTime invalidEndDate = LocalDateTime.now().minusYears(1);
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        doThrow(new BusinessException("End date must be after start date"))
                .when(projectValidator).validateEndDate(testProject.getStartDate(), invalidEndDate);

        // Act & Assert
        assertThrows(BusinessException.class, () -> projectService.updateProjectEndDate(testId, invalidEndDate));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void updateProjectEndDate_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        LocalDateTime newEndDate = LocalDateTime.now().plusMonths(6);
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.updateProjectEndDate(testId, newEndDate));
    }

    @Test
    void updateProjectStatus_ShouldUpdateStatus() {
        // Arrange
        ProjectStatus newStatus = ProjectStatus.COMPLETED;
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.updateProjectStatus(testId, newStatus);

        // Assert
        assertNotNull(result);
        assertEquals(newStatus, testProject.getStatus());
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void updateProjectStatus_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        ProjectStatus newStatus = ProjectStatus.COMPLETED;
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.updateProjectStatus(testId, newStatus));
    }

    @Test
    void getProject_WhenProjectExists_ShouldReturnProject() {
        // Arrange
        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.getProject(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(projectRepositoryHelper, times(1)).findByIdOrThrow(testId);
    }

    @Test
    void getProject_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectService.getProject(testId));
    }

    @Test
    void getAllProjects_ShouldReturnProjectsList() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Project> projectPage = new PageImpl<>(List.of(testProject));

        when(projectRepository.findAll(pageable)).thenReturn(projectPage);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        List<ProjectResponse> result = projectService.getAllProjects(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findAll(pageable);
    }

    @Test
    void getProjectsByStatus_ShouldReturnFilteredProjects() {
        // Arrange
        ProjectStatus status = ProjectStatus.IN_PROGRESS;
        List<Project> projects = List.of(testProject);

        when(projectRepository.findByStatus(status)).thenReturn(projects);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        List<ProjectResponse> result = projectService.getProjectsByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findByStatus(status);
    }

    @Test
    void getProjectsBetweenDates_ShouldReturnFilteredProjects() {
        // Arrange
        LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
        LocalDateTime endDate = LocalDateTime.now().plusMonths(1);
        List<Project> projects = List.of(testProject);

        when(projectRepository.findByStartDateAfterAndEndDateBefore(startDate, endDate))
                .thenReturn(projects);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        List<ProjectResponse> result = projectService.getProjectsBetweenDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findByStartDateAfterAndEndDateBefore(startDate, endDate);
    }

    @Test
    void assignProductBacklogToProject_WhenValid_ShouldAssignProductBacklog() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setId(productBacklogId);
        testProject.setProductBacklog(null);

        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));
        when(projectRepository.save(testProject)).thenReturn(testProject);
        when(projectMapper.toResponse(testProject)).thenReturn(testResponse);

        // Act
        ProjectResponse result = projectService.assignProductBacklogToProject(testId, productBacklogId);

        // Assert
        assertNotNull(result);
        assertEquals(productBacklog, testProject.getProductBacklog());
        verify(projectRepository, times(1)).save(testProject);
    }

    @Test
    void assignProductBacklogToProject_WhenProjectNotFound_ShouldThrowException() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();
        when(projectRepositoryHelper.findByIdOrThrow(testId))
                .thenThrow(new EntityNotFoundException("Project not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> projectService.assignProductBacklogToProject(testId, productBacklogId));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void assignProductBacklogToProject_WhenProductBacklogNotFound_ShouldThrowException() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();
        testProject.setProductBacklog(null);

        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> projectService.assignProductBacklogToProject(testId, productBacklogId));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void assignProductBacklogToProject_WhenProjectAlreadyHasBacklog_ShouldThrowException() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();
        ProductBacklog existingBacklog = new ProductBacklog();
        testProject.setProductBacklog(existingBacklog);

        when(projectRepositoryHelper.findByIdOrThrow(testId)).thenReturn(testProject);
        doThrow(new IllegalStateException("This project already has a ProductBacklog assigned"))
                .when(projectValidator).validateProductBacklogAssignment(testProject);

        // Act & Assert
        assertThrows(IllegalStateException.class,
                () -> projectService.assignProductBacklogToProject(testId, productBacklogId));
        verify(productBacklogRepository, never()).findById(any());
        verify(projectRepository, never()).save(any());
    }
}
