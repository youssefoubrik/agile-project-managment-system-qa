package ma.ensa.apms.service.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Task;
import ma.ensa.apms.repository.TaskRepository;

/**
 * Unit tests for TaskRepositoryHelper.
 * 
 * Tests the helper methods for Task repository operations,
 * including retrieval and validation logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("TaskRepositoryHelper Tests")
class TaskRepositoryHelperTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskRepositoryHelper taskRepositoryHelper;

    private UUID taskId;
    private Task task;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task = new Task();
        task.setId(taskId);
    }

    @Test
    @DisplayName("findByIdOrThrow should return task when task exists")
    void findByIdOrThrow_ShouldReturnTask_WhenTaskExists() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // When
        Task result = taskRepositoryHelper.findByIdOrThrow(taskId);

        // Then
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw exception when task does not exist")
    void findByIdOrThrow_ShouldThrowException_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskRepositoryHelper.findByIdOrThrow(taskId));

        assertTrue(exception.getMessage().contains("Task not found with id:"));
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("validateExists should not throw exception when task exists")
    void validateExists_ShouldNotThrowException_WhenTaskExists() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> taskRepositoryHelper.validateExists(taskId));
        verify(taskRepository).existsById(taskId);
    }

    @Test
    @DisplayName("validateExists should throw exception when task does not exist")
    void validateExists_ShouldThrowException_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskRepositoryHelper.validateExists(taskId));

        assertTrue(exception.getMessage().contains("Task not found with id:"));
        verify(taskRepository).existsById(taskId);
    }
}
