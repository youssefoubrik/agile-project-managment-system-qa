package ma.ensa.apms.service.helper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.ProductBacklogRepository;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;

@ExtendWith(MockitoExtension.class)
class UserStoryRepositoryHelperTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private UserStoryRepositoryHelper repositoryHelper;

    private UUID testId;
    private UserStory testUserStory;
    private Epic testEpic;
    private SprintBacklog testSprintBacklog;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testUserStory = new UserStory();
        testUserStory.setId(testId);
        testEpic = new Epic();
        testEpic.setId(testId);
        testSprintBacklog = new SprintBacklog();
        testSprintBacklog.setId(testId);
    }

    @Test
    void findUserStoryById_WhenExists_ShouldReturnUserStory() {
        // Arrange
        when(userStoryRepository.findById(testId)).thenReturn(Optional.of(testUserStory));

        // Act
        UserStory result = repositoryHelper.findUserStoryById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        verify(userStoryRepository, times(1)).findById(testId);
    }

    @Test
    void findUserStoryById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(userStoryRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> repositoryHelper.findUserStoryById(testId));
        verify(userStoryRepository, times(1)).findById(testId);
    }

    @Test
    void findEpicById_WhenExists_ShouldReturnEpic() {
        // Arrange
        when(epicRepository.findById(testId)).thenReturn(Optional.of(testEpic));

        // Act
        Epic result = repositoryHelper.findEpicById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        verify(epicRepository, times(1)).findById(testId);
    }

    @Test
    void findEpicById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(epicRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> repositoryHelper.findEpicById(testId));
        verify(epicRepository, times(1)).findById(testId);
    }

    @Test
    void findSprintBacklogById_WhenExists_ShouldReturnSprintBacklog() {
        // Arrange
        when(sprintBacklogRepository.findById(testId)).thenReturn(Optional.of(testSprintBacklog));

        // Act
        SprintBacklog result = repositoryHelper.findSprintBacklogById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testId, result.getId());
        verify(sprintBacklogRepository, times(1)).findById(testId);
    }

    @Test
    void findSprintBacklogById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(sprintBacklogRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> repositoryHelper.findSprintBacklogById(testId));
        verify(sprintBacklogRepository, times(1)).findById(testId);
    }

    @Test
    void validateProductBacklogExists_WhenExists_ShouldNotThrow() {
        // Arrange
        when(productBacklogRepository.findById(testId))
                .thenReturn(Optional.of(new ma.ensa.apms.modal.ProductBacklog()));

        // Act & Assert
        assertDoesNotThrow(() -> repositoryHelper.validateProductBacklogExists(testId));
        verify(productBacklogRepository, times(1)).findById(testId);
    }

    @Test
    void validateProductBacklogExists_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(productBacklogRepository.findById(testId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> repositoryHelper.validateProductBacklogExists(testId));
        verify(productBacklogRepository, times(1)).findById(testId);
    }
}
