package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

/**
 * Helper class for User Story repository operations
 * Reduces complexity by centralizing entity retrieval logic
 */
@Component
@RequiredArgsConstructor
public class UserStoryRepositoryHelper {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final ProductBacklogRepository productBacklogRepository;

    /**
     * Find a user story by ID
     * 
     * @param id the id of the user story
     * @return the user story entity
     * @throws ResourceNotFoundException if the user story is not found
     */
    public UserStory findUserStoryById(UUID id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User story not found"));
    }

    /**
     * Find an epic by ID
     * 
     * @param id the id of the epic
     * @return the epic entity
     * @throws ResourceNotFoundException if the epic is not found
     */
    public Epic findEpicById(UUID id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found"));
    }

    /**
     * Find a sprint backlog by ID
     * 
     * @param id the id of the sprint backlog
     * @return the sprint backlog entity
     * @throws ResourceNotFoundException if the sprint backlog is not found
     */
    public SprintBacklog findSprintBacklogById(UUID id) {
        return sprintBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint Backlog not found"));
    }

    /**
     * Validate that the product backlog exists
     * 
     * @param id the id of the product backlog
     * @throws ResourceNotFoundException if the product backlog is not found
     */
    public void validateProductBacklogExists(UUID id) {
        productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Backlog not found"));
    }
}
