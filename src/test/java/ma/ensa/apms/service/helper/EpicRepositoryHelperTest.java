package ma.ensa.apms.service.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
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
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;

/**
 * Unit tests for EpicRepositoryHelper.
 * 
 * Tests the helper methods for Epic repository operations,
 * including retrieval, validation, and user story count logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EpicRepositoryHelper Tests")
class EpicRepositoryHelperTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private EpicRepositoryHelper epicRepositoryHelper;

    private UUID epicId;
    private UUID userStoryId;
    private Epic epic;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        epicId = UUID.randomUUID();
        userStoryId = UUID.randomUUID();

        epic = new Epic();
        epic.setId(epicId);
        epic.setUserStories(new ArrayList<>());

        userStory = new UserStory();
        userStory.setId(userStoryId);
    }

    @Test
    @DisplayName("findByIdOrThrow should return epic when epic exists")
    void findByIdOrThrow_ShouldReturnEpic_WhenEpicExists() {
        // Given
        when(epicRepository.findById(epicId)).thenReturn(Optional.of(epic));

        // When
        Epic result = epicRepositoryHelper.findByIdOrThrow(epicId);

        // Then
        assertNotNull(result);
        assertEquals(epicId, result.getId());
        verify(epicRepository).findById(epicId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw exception when epic does not exist")
    void findByIdOrThrow_ShouldThrowException_WhenEpicDoesNotExist() {
        // Given
        when(epicRepository.findById(epicId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> epicRepositoryHelper.findByIdOrThrow(epicId));

        assertTrue(exception.getMessage().contains("Epic not found with id:"));
        verify(epicRepository).findById(epicId);
    }

    @Test
    @DisplayName("findUserStoryByIdOrThrow should return user story when it exists")
    void findUserStoryByIdOrThrow_ShouldReturnUserStory_WhenUserStoryExists() {
        // Given
        when(userStoryRepository.findById(userStoryId)).thenReturn(Optional.of(userStory));

        // When
        UserStory result = epicRepositoryHelper.findUserStoryByIdOrThrow(userStoryId);

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
                () -> epicRepositoryHelper.findUserStoryByIdOrThrow(userStoryId));

        assertTrue(exception.getMessage().contains("UserStory not found with id:"));
        verify(userStoryRepository).findById(userStoryId);
    }

    @Test
    @DisplayName("getUserStoriesCount should return 0 when user stories list is null")
    void getUserStoriesCount_ShouldReturnZero_WhenUserStoriesListIsNull() {
        // Given
        epic.setUserStories(null);

        // When
        int count = epicRepositoryHelper.getUserStoriesCount(epic);

        // Then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("getUserStoriesCount should return 0 when user stories list is empty")
    void getUserStoriesCount_ShouldReturnZero_WhenUserStoriesListIsEmpty() {
        // Given
        epic.setUserStories(new ArrayList<>());

        // When
        int count = epicRepositoryHelper.getUserStoriesCount(epic);

        // Then
        assertEquals(0, count);
    }

    @Test
    @DisplayName("getUserStoriesCount should return correct count when user stories exist")
    void getUserStoriesCount_ShouldReturnCorrectCount_WhenUserStoriesExist() {
        // Given
        List<UserStory> userStories = new ArrayList<>();
        userStories.add(new UserStory());
        userStories.add(new UserStory());
        userStories.add(new UserStory());
        epic.setUserStories(userStories);

        // When
        int count = epicRepositoryHelper.getUserStoriesCount(epic);

        // Then
        assertEquals(3, count);
    }

    @Test
    @DisplayName("validateExists should not throw exception when epic exists")
    void validateExists_ShouldNotThrowException_WhenEpicExists() {
        // Given
        when(epicRepository.existsById(epicId)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> epicRepositoryHelper.validateExists(epicId));
        verify(epicRepository).existsById(epicId);
    }

    @Test
    @DisplayName("validateExists should throw exception when epic does not exist")
    void validateExists_ShouldThrowException_WhenEpicDoesNotExist() {
        // Given
        when(epicRepository.existsById(epicId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> epicRepositoryHelper.validateExists(epicId));

        assertTrue(exception.getMessage().contains("Epic not found with id:"));
        verify(epicRepository).existsById(epicId);
    }
}
