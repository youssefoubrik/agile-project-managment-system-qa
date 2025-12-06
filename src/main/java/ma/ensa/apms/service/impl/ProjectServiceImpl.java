package ma.ensa.apms.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ma.ensa.apms.annotation.LogOperation;
import ma.ensa.apms.dto.Request.ProjectRequest;
import ma.ensa.apms.dto.Response.ProjectResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.ProjectMapper;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.ProjectRepository;
import ma.ensa.apms.service.ProjectService;
import ma.ensa.apms.service.helper.ProjectRepositoryHelper;
import ma.ensa.apms.service.validator.ProjectValidator;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProductBacklogRepository productBacklogRepository;
    private final ProjectRepositoryHelper projectRepositoryHelper;
    private final ProjectValidator projectValidator;

    @Override
    @LogOperation(description = "Creating new project")
    public ProjectResponse createProject(ProjectRequest request) {
        Project project = projectMapper.toEntity(request);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    @LogOperation(description = "Updating project")
    public ProjectResponse updateProject(UUID id, ProjectRequest request) {
        Project project = projectRepositoryHelper.findByIdOrThrow(id);
        projectMapper.updateEntityFromRequest(request, project);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public void deleteProject(UUID id) {
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectResponse updateProjectStartDate(UUID id, LocalDateTime startDate) {
        Project project = projectRepositoryHelper.findByIdOrThrow(id);
        projectValidator.validateStartDate(startDate, project.getEndDate());
        project.setStartDate(startDate);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProjectEndDate(UUID id, LocalDateTime endDate) {
        Project project = projectRepositoryHelper.findByIdOrThrow(id);
        projectValidator.validateEndDate(project.getStartDate(), endDate);
        project.setEndDate(endDate);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse updateProjectStatus(UUID id, ProjectStatus status) {
        Project project = projectRepositoryHelper.findByIdOrThrow(id);
        project.setStatus(status);
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    @LogOperation(description = "Getting project by ID")
    public ProjectResponse getProject(UUID id) {
        Project project = projectRepositoryHelper.findByIdOrThrow(id);
        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> getAllProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProjectResponse> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProjectResponse> getProjectsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return projectRepository.findByStartDateAfterAndEndDateBefore(startDate, endDate)
                .stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProjectResponse assignProductBacklogToProject(UUID projectId, UUID productBacklogId) {
        Project project = projectRepositoryHelper.findByIdOrThrow(projectId);
        projectValidator.validateProductBacklogAssignment(project);

        ProductBacklog productBacklog = productBacklogRepository.findById(productBacklogId)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));

        project.setProductBacklog(productBacklog);
        projectRepository.save(project);

        return projectMapper.toResponse(project);
    }
}
