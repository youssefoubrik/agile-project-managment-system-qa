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
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

/**
 * Unit tests for SprintBacklogRepositoryHelper.
 * 
 * Tests the helper methods for SprintBacklog repository operations,
 * including retrieval and validation logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SprintBacklogRepositoryHelper Tests")
class SprintBacklogRepositoryHelperTest {

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private SprintBacklogRepositoryHelper sprintBacklogRepositoryHelper;

    private UUID sprintBacklogId;
    private UUID userStoryId;
    private SprintBacklog sprintBacklog;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        sprintBacklogId = UUID.randomUUID();
        userStoryId = UUID.randomUUID();

        sprintBacklog = new SprintBacklog();
        sprintBacklog.setId(sprintBacklogId);

        userStory = new UserStory();
        userStory.setId(userStoryId);
    }

    @Test
    @DisplayName("findByIdOrThrow should return sprint backlog when it exists")
    void findByIdOrThrow_ShouldReturnSprintBacklog_WhenSprintBacklogExists() {
        // Given
        when(sprintBacklogRepository.findById(sprintBacklogId)).thenReturn(Optional.of(sprintBacklog));

        // When
        SprintBacklog result = sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklogId);

        // Then
        assertNotNull(result);
        assertEquals(sprintBacklogId, result.getId());
        verify(sprintBacklogRepository).findById(sprintBacklogId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw exception when sprint backlog does not exist")
    void findByIdOrThrow_ShouldThrowException_WhenSprintBacklogDoesNotExist() {
        // Given
        when(sprintBacklogRepository.findById(sprintBacklogId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklogId));

        assertTrue(exception.getMessage().contains("Sprint backlog not found"));
        verify(sprintBacklogRepository).findById(sprintBacklogId);
    }

    @Test
    @DisplayName("findUserStoryByIdOrThrow should return user story when it exists")
    void findUserStoryByIdOrThrow_ShouldReturnUserStory_WhenUserStoryExists() {
        // Given
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        // When
        UserStory result = sprintBacklogRepositoryHelper.findUserStoryByIdOrThrow(userStoryId);

        // Then
        assertNotNull(result);
        assertEquals(userStoryId, result.getId());
        verify(userStoryRepository).findById(userStoryId);
    }

    @Test
    @DisplayName("findUserStoryByIdOrThrow should throw exception when user story does not exist")
    void findUserStoryByIdOrThrow_ShouldThrowException_WhenUserStoryDoesNotExist() {
        // Given
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> sprintBacklogRepositoryHelper.findUserStoryByIdOrThrow(userStoryId));

        assertTrue(exception.getMessage().contains("User story not found"));
        verify(userStoryRepository).findById(userStoryId);
    }

    @Test
    @DisplayName("validateExists should not throw exception when sprint backlog exists")
    void validateExists_ShouldNotThrowException_WhenSprintBacklogExists() {
        // Given
        when(sprintBacklogRepository.existsById(sprintBacklogId)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> sprintBacklogRepositoryHelper.validateExists(sprintBacklogId));
        verify(sprintBacklogRepository).existsById(sprintBacklogId);
    }

    @Test
    @DisplayName("validateExists should throw exception when sprint backlog does not exist")
    void validateExists_ShouldThrowException_WhenSprintBacklogDoesNotExist() {
        // Given
        when(sprintBacklogRepository.existsById(sprintBacklogId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> sprintBacklogRepositoryHelper.validateExists(sprintBacklogId));

        assertTrue(exception.getMessage().contains("Sprint backlog not found"));
        verify(sprintBacklogRepository).existsById(sprintBacklogId);
    }
}
