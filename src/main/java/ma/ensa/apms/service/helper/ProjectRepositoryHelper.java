package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import ma.ensa.apms.modal.Project;
import ma.ensa.apms.repository.ProjectRepository;

/**
 * Helper class for Project repository operations.
 * Provides centralized methods for retrieving projects with proper error
 * handling.
 */
@Component
@AllArgsConstructor
public class ProjectRepositoryHelper {
    private final ProjectRepository projectRepository;

    /**
     * Find a project by ID or throw EntityNotFoundException
     *
     * @param id the project ID
     * @return the found Project entity
     * @throws EntityNotFoundException if project not found
     */
    public Project findByIdOrThrow(UUID id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }
}
