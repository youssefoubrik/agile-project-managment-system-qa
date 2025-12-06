package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryStatus;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.helper.UserStoryRepositoryHelper;
import ma.ensa.apms.service.validator.UserStoryValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceImplTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @Mock
    private UserStoryRepositoryHelper repositoryHelper;

    @Mock
    private UserStoryValidator validator;

    @InjectMocks
    private UserStoryServiceImpl userStoryService;

    private UUID testId;
    private UserStory testUserStory;
    private UserStoryRequest testRequest;
    private UserStoryResponse testResponse;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testUserStory = new UserStory();
        testUserStory.setId(testId);
        testUserStory.setStatus(UserStoryStatus.TODO);
        testUserStory.setName("Test User Story");
        testUserStory.setRole("Test Role");
        testUserStory.setFeature("Test Feature");
        testUserStory.setAcceptanceCriterias(new ArrayList<>());

        testRequest = new UserStoryRequest();

        testResponse = new UserStoryResponse();
        testResponse.setId(testId);
        testResponse.setStatus(UserStoryStatus.TODO);
    }

    @Test
    void create_ShouldCreateUserStory() {
        // Arrange
        when(userStoryMapper.toEntity(testRequest)).thenReturn(testUserStory);
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.create(testRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        assertEquals(UserStoryStatus.TODO, testUserStory.getStatus());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    @Test
    void updateUserStory_ShouldUpdateUserStory() {
        // Arrange
        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(userStoryRepository.save(testUserStory)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.updateUserStory(testId, testRequest);

        // Assert
        assertNotNull(result);
        verify(repositoryHelper, times(1)).findUserStoryById(testId);
        verify(userStoryMapper, times(1)).updateEntityFromDto(testRequest, testUserStory);
        verify(userStoryRepository, times(1)).save(testUserStory);
    }

    @Test
    void getUserStoryById_ShouldReturnUserStory() {
        // Arrange
        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.getUserStoryById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testResponse.getId(), result.getId());
        verify(repositoryHelper, times(1)).findUserStoryById(testId);
    }

    @Test
    void changeStatus_ToDone_ShouldValidateAndChangeStatus() {
        // Arrange
        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(userStoryRepository.save(testUserStory)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.changeStatus(testId, UserStoryStatus.DONE);

        // Assert
        assertNotNull(result);
        assertEquals(UserStoryStatus.DONE, testUserStory.getStatus());
        verify(validator, times(1)).validateCanMarkAsDone(testUserStory);
        verify(userStoryRepository, times(1)).save(testUserStory);
    }

    @Test
    void changeStatus_ToInProgress_ShouldNotValidate() {
        // Arrange
        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(userStoryRepository.save(testUserStory)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.changeStatus(testId, UserStoryStatus.IN_PROGRESS);

        // Assert
        assertNotNull(result);
        assertEquals(UserStoryStatus.IN_PROGRESS, testUserStory.getStatus());
        verify(validator, never()).validateCanMarkAsDone(any());
        verify(userStoryRepository, times(1)).save(testUserStory);
    }

    @Test
    void linkToEpic_ShouldLinkUserStoryToEpic() {
        // Arrange
        UUID epicId = UUID.randomUUID();
        Epic epic = new Epic();
        epic.setId(epicId);

        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(repositoryHelper.findEpicById(epicId)).thenReturn(epic);
        when(userStoryRepository.save(testUserStory)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.linkToEpic(testId, epicId);

        // Assert
        assertNotNull(result);
        assertEquals(epic, testUserStory.getEpic());
        verify(validator, times(1)).validateCanLinkToEpic(testUserStory);
        verify(userStoryRepository, times(1)).save(testUserStory);
    }

    @Test
    void moveToSprint_ShouldMoveUserStoryToSprint() {
        // Arrange
        UUID sprintId = UUID.randomUUID();
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(sprintId);

        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(repositoryHelper.findSprintBacklogById(sprintId)).thenReturn(sprint);
        when(userStoryRepository.save(testUserStory)).thenReturn(testUserStory);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        UserStoryResponse result = userStoryService.moveToSprint(testId, sprintId);

        // Assert
        assertNotNull(result);
        assertEquals(sprint, testUserStory.getSprintBacklog());
        assertNull(testUserStory.getProductBacklog());
        verify(userStoryRepository, times(1)).save(testUserStory);
    }

    @Test
    void getAcceptanceCriteriasByUserStoryId_ShouldReturnCriteriaList() {
        // Arrange
        AcceptanceCriteria criteria1 = new AcceptanceCriteria();
        AcceptanceCriteria criteria2 = new AcceptanceCriteria();
        List<AcceptanceCriteria> criteriaList = List.of(criteria1, criteria2);
        testUserStory.setAcceptanceCriterias(criteriaList);

        AcceptanceCriteriaResponse response1 = new AcceptanceCriteriaResponse();
        AcceptanceCriteriaResponse response2 = new AcceptanceCriteriaResponse();

        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);
        when(acceptanceCriteriaMapper.toDto(criteria1)).thenReturn(response1);
        when(acceptanceCriteriaMapper.toDto(criteria2)).thenReturn(response2);

        // Act
        List<AcceptanceCriteriaResponse> result = userStoryService.getAcceptanceCriteriasByUserStoryId(testId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repositoryHelper, times(1)).findUserStoryById(testId);
    }

    @Test
    void getUserStoriesByStatusAndProductBacklogId_ShouldReturnFilteredList() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();
        List<UserStory> userStories = List.of(testUserStory);

        when(userStoryRepository.findByStatusAndProductBacklogId(UserStoryStatus.TODO, productBacklogId))
                .thenReturn(userStories);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        List<UserStoryResponse> result = userStoryService.getUserStoriesByStatusAndProductBacklogId(
                UserStoryStatus.TODO, productBacklogId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repositoryHelper, times(1)).validateProductBacklogExists(productBacklogId);
    }

    @Test
    void getUserStoriesByStatusAndProductBacklogId_WithNullStatus_ShouldThrowException() {
        // Arrange
        UUID productBacklogId = UUID.randomUUID();

        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> userStoryService.getUserStoriesByStatusAndProductBacklogId(null, productBacklogId));
    }

    @Test
    void getUserStoriesByStatusAndProductBacklogId_WithNullProductBacklogId_ShouldThrowException() {
        // Act & Assert
        assertThrows(NullPointerException.class,
                () -> userStoryService.getUserStoriesByStatusAndProductBacklogId(UserStoryStatus.TODO, null));
    }

    @Test
    void getUserStoriesByEpicId_ShouldReturnUserStoriesList() {
        // Arrange
        UUID epicId = UUID.randomUUID();
        Epic epic = new Epic();
        epic.setId(epicId);
        List<UserStory> userStories = List.of(testUserStory);

        when(repositoryHelper.findEpicById(epicId)).thenReturn(epic);
        when(userStoryRepository.findByEpicId(epicId)).thenReturn(userStories);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        List<UserStoryResponse> result = userStoryService.getUserStoriesByEpicId(epicId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(repositoryHelper, times(1)).findEpicById(epicId);
    }

    @Test
    void getUserStoriesByEpicId_WithNullEpicId_ShouldThrowException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userStoryService.getUserStoriesByEpicId(null));
    }

    @Test
    void getUserStoriesBySprintBacklogId_ShouldReturnUserStoriesList() {
        // Arrange
        UUID sprintId = UUID.randomUUID();
        List<UserStory> userStories = List.of(testUserStory);

        when(userStoryRepository.findBySprintBacklogId(sprintId)).thenReturn(userStories);
        when(userStoryMapper.toResponse(testUserStory)).thenReturn(testResponse);

        // Act
        List<UserStoryResponse> result = userStoryService.getUserStoriesBySprintBacklogId(sprintId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getUserStoriesBySprintBacklogId_WithNullSprintId_ShouldThrowException() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> userStoryService.getUserStoriesBySprintBacklogId(null));
    }

    @Test
    void delete_ShouldValidateAndDeleteUserStory() {
        // Arrange
        when(repositoryHelper.findUserStoryById(testId)).thenReturn(testUserStory);

        // Act
        userStoryService.delete(testId);

        // Assert
        verify(validator, times(1)).validateCanDelete(testUserStory);
        verify(userStoryRepository, times(1)).deleteById(testId);
    }
}
