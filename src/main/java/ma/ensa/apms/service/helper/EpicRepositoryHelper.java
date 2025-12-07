package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;

/**
 * Helper class for Epic repository operations.
 * 
 * <p>
 * Encapsulates common repository operations and business logic for Epics
 * to reduce complexity in the service layer and promote code reuse.
 * </p>
 * 
 * <p>
 * <b>Responsibilities:</b>
 * </p>
 * <ul>
 * <li>Epic retrieval with proper exception handling</li>
 * <li>UserStory retrieval for Epic operations</li>
 * <li>User stories count calculation</li>
 * <li>Centralized error message management</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class EpicRepositoryHelper {

    private final EpicRepository epicRepository;
    private final UserStoryRepository userStoryRepository;

    /**
     * Finds an epic by ID or throws ResourceNotFoundException.
     * 
     * @param id the epic ID
     * @return the found epic
     * @throws ResourceNotFoundException if epic not found
     */
    public Epic findByIdOrThrow(UUID id) {
        return epicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Epic not found with id: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("UserStory not found with id: " + id));
    }

    /**
     * Gets the count of user stories for an epic.
     * 
     * @param epic the epic
     * @return the count of user stories
     */
    public int getUserStoriesCount(Epic epic) {
        return epic.getUserStories() != null ? epic.getUserStories().size() : 0;
    }

    /**
     * Validates that an epic exists by ID.
     * 
     * @param id the epic ID
     * @throws ResourceNotFoundException if epic does not exist
     */
    public void validateExists(UUID id) {
        if (!epicRepository.existsById(id)) {
            throw new ResourceNotFoundException("Epic not found with id: " + id);
        }
    }
}
