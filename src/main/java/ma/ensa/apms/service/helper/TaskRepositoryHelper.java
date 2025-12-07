package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Task;
import ma.ensa.apms.repository.TaskRepository;

/**
 * Helper class for Task repository operations.
 * 
 * <p>
 * Encapsulates common repository operations and validations for Tasks
 * to reduce complexity in the service layer and promote code reuse.
 * </p>
 * 
 * <p>
 * <b>Responsibilities:</b>
 * </p>
 * <ul>
 * <li>Task retrieval with proper exception handling</li>
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
public class TaskRepositoryHelper {

    private final TaskRepository taskRepository;

    /**
     * Finds a task by ID or throws ResourceNotFoundException.
     * 
     * @param id the task ID
     * @return the found task
     * @throws ResourceNotFoundException if task not found
     */
    public Task findByIdOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    /**
     * Validates that a task exists by ID.
     * 
     * @param id the task ID
     * @throws ResourceNotFoundException if task does not exist
     */
    public void validateExists(UUID id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id: " + id);
        }
    }
}
