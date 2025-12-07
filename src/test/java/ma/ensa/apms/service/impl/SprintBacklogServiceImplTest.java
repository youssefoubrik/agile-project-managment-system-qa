package ma.ensa.apms.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.ensa.apms.dto.Request.SprintBacklogRequest;
import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.SprintBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.SprintBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.SprintBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.SprintBacklogRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.helper.SprintBacklogRepositoryHelper;

class SprintBacklogServiceImplTest {

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private SprintBacklogMapper sprintBacklogMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private SprintBacklogRepositoryHelper sprintBacklogRepositoryHelper;

    @InjectMocks
    private SprintBacklogServiceImpl sprintBacklogService;

    private SprintBacklog sprintBacklog;
    private SprintBacklogRequest sprintBacklogRequest;
    private SprintBacklogResponse sprintBacklogResponse;
    private UserStory userStory;
    private UserStoryRequest userStoryRequest;
    private UserStoryResponse userStoryResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID sprintBacklogId = UUID.randomUUID();
        UUID userStoryId = UUID.randomUUID();

        sprintBacklog = SprintBacklog.builder()
                .id(sprintBacklogId)
                .name("Sprint Backlog 1")
                .userStories(Collections.emptyList())
                .build();

        sprintBacklogRequest = SprintBacklogRequest.builder()
                .name("Sprint Backlog 1")
                .build();

        sprintBacklogResponse = SprintBacklogResponse.builder()
                .id(sprintBacklogId)
                .name("Sprint Backlog 1")
                .build();

        userStory = UserStory.builder()
                .id(userStoryId)
                .name("User Story 1")
                .build();

        userStoryRequest = UserStoryRequest.builder()
                .name("User Story 1")
                .role("As a user")
                .feature("I want to create a feature")
                .benefit("So that I can achieve a goal")
                .priority(1)
                .status(null)
                .build();

        userStoryResponse = UserStoryResponse.builder()
                .id(userStoryId)
                .name("User Story 1")
                .build();
    }

    @Test
    void createSprintBacklog_ShouldReturnCreatedSprintBacklogResponse() {
        when(sprintBacklogMapper.toEntity(sprintBacklogRequest)).thenReturn(sprintBacklog);
        when(sprintBacklogRepository.save(sprintBacklog)).thenReturn(sprintBacklog);
        when(sprintBacklogMapper.toResponse(sprintBacklog)).thenReturn(sprintBacklogResponse);

        SprintBacklogResponse result = sprintBacklogService.createSprintBacklog(sprintBacklogRequest);

        assertNotNull(result);
        assertEquals(sprintBacklogResponse, result);
        verify(sprintBacklogRepository, times(1)).save(sprintBacklog);
    }

    @Test
    void getSprintBacklogById_ShouldReturnSprintBacklogResponse_WhenFound() {
        when(sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklog.getId())).thenReturn(sprintBacklog);
        when(sprintBacklogMapper.toResponse(sprintBacklog)).thenReturn(sprintBacklogResponse);

        SprintBacklogResponse result = sprintBacklogService.getSprintBacklogById(sprintBacklog.getId());

        assertNotNull(result);
        assertEquals(sprintBacklogResponse, result);
        verify(sprintBacklogRepositoryHelper, times(1)).findByIdOrThrow(sprintBacklog.getId());
    }

    @Test
    void getSprintBacklogById_ShouldThrowException_WhenNotFound() {
        when(sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklog.getId()))
                .thenThrow(new ResourceNotFoundException("SprintBacklog not found with id: " + sprintBacklog.getId()));

        assertThrows(ResourceNotFoundException.class,
                () -> sprintBacklogService.getSprintBacklogById(sprintBacklog.getId()));
        verify(sprintBacklogRepositoryHelper, times(1)).findByIdOrThrow(sprintBacklog.getId());
    }

    @Test
    void getAllSprintBacklogs_ShouldReturnListOfSprintBacklogResponses() {
        when(sprintBacklogRepository.findAll()).thenReturn(Collections.singletonList(sprintBacklog));
        when(sprintBacklogMapper.toResponse(sprintBacklog)).thenReturn(sprintBacklogResponse);

        List<SprintBacklogResponse> result = sprintBacklogService.getAllSprintBacklogs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(sprintBacklogResponse, result.get(0));
        verify(sprintBacklogRepository, times(1)).findAll();
    }

    @Test
    void updateSprintBacklog_ShouldReturnUpdatedSprintBacklogResponse() {
        when(sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklog.getId())).thenReturn(sprintBacklog);
        doNothing().when(sprintBacklogMapper).updateFromDto(sprintBacklogRequest, sprintBacklog);
        when(sprintBacklogRepository.save(sprintBacklog)).thenReturn(sprintBacklog);
        when(sprintBacklogMapper.toResponse(sprintBacklog)).thenReturn(sprintBacklogResponse);

        SprintBacklogResponse result = sprintBacklogService.updateSprintBacklog(sprintBacklog.getId(),
                sprintBacklogRequest);

        assertNotNull(result);
        assertEquals(sprintBacklogResponse, result);
        verify(sprintBacklogRepositoryHelper, times(1)).findByIdOrThrow(sprintBacklog.getId());
        verify(sprintBacklogRepository, times(1)).save(sprintBacklog);
    }

    @Test
    void deleteSprintBacklog_ShouldDeleteSprintBacklog_WhenFound() {
        doNothing().when(sprintBacklogRepositoryHelper).validateExists(sprintBacklog.getId());

        sprintBacklogService.deleteSprintBacklog(sprintBacklog.getId());

        verify(sprintBacklogRepositoryHelper, times(1)).validateExists(sprintBacklog.getId());
        verify(sprintBacklogRepository, times(1)).deleteById(sprintBacklog.getId());
    }

    @Test
    void deleteSprintBacklog_ShouldThrowException_WhenNotFound() {
        doThrow(new ResourceNotFoundException("SprintBacklog not found with id: " + sprintBacklog.getId()))
                .when(sprintBacklogRepositoryHelper).validateExists(sprintBacklog.getId());

        assertThrows(ResourceNotFoundException.class,
                () -> sprintBacklogService.deleteSprintBacklog(sprintBacklog.getId()));
        verify(sprintBacklogRepositoryHelper, times(1)).validateExists(sprintBacklog.getId());
    }

    @Test
    void addUserStoryToSprintBacklog_ShouldReturnCreatedUserStoryResponse() {
        when(sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklog.getId())).thenReturn(sprintBacklog);
        when(userStoryMapper.toEntity(userStoryRequest)).thenReturn(userStory);
        when(userStoryRepository.save(userStory)).thenReturn(userStory);
        when(userStoryMapper.toResponse(userStory)).thenReturn(userStoryResponse);

        UserStoryResponse result = sprintBacklogService.addUserStoryToSprintBacklog(sprintBacklog.getId(),
                userStoryRequest);

        assertNotNull(result);
        assertEquals(userStoryResponse, result);
        verify(userStoryRepository, times(1)).save(userStory);
    }

    @Test
    void removeUserStoryFromSprintBacklog_ShouldRemoveUserStory_WhenFound() {
        when(sprintBacklogRepositoryHelper.findByIdOrThrow(sprintBacklog.getId())).thenReturn(sprintBacklog);
        when(sprintBacklogRepositoryHelper.findUserStoryByIdOrThrow(userStory.getId())).thenReturn(userStory);
        userStory.setSprintBacklog(sprintBacklog);

        sprintBacklogService.removeUserStoryFromSprintBacklog(sprintBacklog.getId(), userStory.getId());

        verify(userStoryRepository, times(1)).delete(userStory);
    }
}