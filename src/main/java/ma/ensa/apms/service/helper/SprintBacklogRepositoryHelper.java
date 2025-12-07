package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

/**
 * Helper class for SprintBacklog repository operations.
 * 
 * <p>
 * Encapsulates common repository operations and validations for SprintBacklogs
 * to reduce complexity in the service layer and promote code reuse.
 * </p>
 * 
 * <p>
 * <b>Responsibilities:</b>
 * </p>
 * <ul>
 * <li>SprintBacklog retrieval with proper exception handling</li>
 * <li>UserStory retrieval for SprintBacklog operations</li>
 * <li>Existence validation</li>
 * <li>Centralized error message management</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class SprintBacklogRepositoryHelper {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryRepository userStoryRepository;

    /**
     * Finds a sprint backlog by ID or throws ResourceNotFoundException.
     * 
     * @param id the sprint backlog ID
     * @return the found sprint backlog
     * @throws ResourceNotFoundException if sprint backlog not found
     */
    public SprintBacklog findByIdOrThrow(UUID id) {
        return sprintBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint backlog not found"));
    }

    /**
     * Finds a user story by ID or throws ResourceNotFoundException.
     * 
     * @param id the user story ID
     * @return the found user story
     * @throws ResourceNotFoundException if user story not found
     */
    public UserStory findUserStoryByIdOrThrow(UUID id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
    }

    /**
     * Validates that a sprint backlog exists by ID.
     * 
     * @param id the sprint backlog ID
     * @throws ResourceNotFoundException if sprint backlog does not exist
     */
    public void validateExists(UUID id) {
        if (!sprintBacklogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sprint backlog not found");
        }
    }
}
