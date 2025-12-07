package ma.ensa.apms.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ma.ensa.apms.dto.Request.EpicRequest;
import ma.ensa.apms.dto.Response.EpicResponse;
import ma.ensa.apms.dto.Response.ProductBacklogResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.EpicMapper;
import ma.ensa.apms.mapper.ProductBacklogMapper;
import ma.ensa.apms.mapper.UserStoryMapper;
import ma.ensa.apms.modal.Epic;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.EpicRepository;
import ma.ensa.apms.repository.UserStoryRepository;
import ma.ensa.apms.service.helper.EpicRepositoryHelper;

@ExtendWith(MockitoExtension.class)
class EpicServiceImplTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private EpicMapper epicMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @Mock
    private EpicRepositoryHelper epicRepositoryHelper;

    @InjectMocks
    private EpicServiceImpl epicService;

    private UUID epicId;
    private Epic epic;
    private EpicRequest epicRequest;
    private EpicResponse epicResponse;
    private UserStory userStory;
    private UUID userStoryId;
    private UserStoryResponse userStoryResponse;
    private ProductBacklog productBacklog;
    private ProductBacklogResponse productBacklogResponse;
    private List<Epic> epicList;
    private List<UserStory> userStoryList;

    @BeforeEach
    void setUp() {
        // Initialize test data
        epicId = UUID.randomUUID();
        userStoryId = UUID.randomUUID();

        epicRequest = new EpicRequest();
        epicRequest.setName("Test Epic");
        epicRequest.setDescription("Test Epic Description");

        epic = new Epic();
        epic.setId(epicId);
        epic.setName("Test Epic");
        epic.setDescription("Test Epic Description");

        userStory = new UserStory();
        userStory.setId(userStoryId);

        userStoryResponse = new UserStoryResponse();
        userStoryResponse.setId(userStoryId);

        epicResponse = new EpicResponse();
        epicResponse.setId(epicId);
        epicResponse.setName("Test Epic");
        epicResponse.setDescription("Test Epic Description");

        productBacklog = new ProductBacklog();
        productBacklog.setId(UUID.randomUUID());

        productBacklogResponse = new ProductBacklogResponse();
        productBacklogResponse.setId(productBacklog.getId());

        userStoryList = new ArrayList<>();
        userStoryList.add(userStory);
        epic.setUserStories(userStoryList);
        epic.setProductBacklog(productBacklog);

        epicList = new ArrayList<>();
        epicList.add(epic);
    }

    @Test
    void testCreate() {
        // Setup
        when(epicMapper.toEntity(epicRequest)).thenReturn(epic);
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.create(epicRequest);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepository).save(epic);
        verify(epicMapper).toEntity(epicRequest);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testFindById() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);
        when(epicRepositoryHelper.getUserStoriesCount(epic)).thenReturn(1);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.findById(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        assertEquals(1, result.getUserStoriesCount());
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testFindById_NotFound() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId))
                .thenThrow(new ResourceNotFoundException("Epic not found with id: " + epicId));

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () -> epicService.findById(epicId));
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
    }

    @Test
    void testFindAll() {
        // Setup
        when(epicRepository.findAll()).thenReturn(epicList);
        when(epicRepositoryHelper.getUserStoriesCount(epic)).thenReturn(1);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        List<EpicResponse> result = epicService.findAll();

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(epicRepository).findAll();
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testUpdate() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.update(epicId, epicRequest);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(epicMapper).updateEntityFromDto(epicRequest, epic);
        verify(epicRepository).save(epic);
        verify(epicMapper).toDto(epic);
    }

    @Test
    void testDelete() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);

        // Execute
        epicService.delete(epicId);

        // Verify
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(epicRepository).delete(epic);
    }

    @Test
    void testAddUserStoryToEpic() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);
        when(epicRepositoryHelper.findUserStoryByIdOrThrow(userStoryId)).thenReturn(userStory);
        when(epicMapper.toDto(epic)).thenReturn(epicResponse);

        // Execute
        EpicResponse result = epicService.addUserStoryToEpic(epicId, userStoryId);

        // Verify
        assertNotNull(result);
        assertEquals(epicResponse, result);
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(epicRepositoryHelper).findUserStoryByIdOrThrow(userStoryId);
        verify(userStoryRepository).save(userStory);
        verify(epicMapper).toDto(epic);
        assertEquals(epic, userStory.getEpic());
    }

    @Test
    void testGetUserStoriesByEpicId() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);
        when(userStoryMapper.toResponse(userStory)).thenReturn(userStoryResponse);

        // Execute
        List<UserStoryResponse> result = epicService.getUserStoriesByEpicId(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(userStoryMapper).toResponse(userStory);
    }

    @Test
    void testGetProductBacklogByEpicId() {
        // Setup
        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epic);
        when(productBacklogMapper.toResponse(productBacklog)).thenReturn(productBacklogResponse);

        // Execute
        ProductBacklogResponse result = epicService.getProductBacklogByEpicId(epicId);

        // Verify
        assertNotNull(result);
        assertEquals(productBacklogResponse, result);
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
        verify(productBacklogMapper).toResponse(productBacklog);
    }

    @Test
    void testGetProductBacklogByEpicId_NoProductBacklog() {
        // Setup
        Epic epicWithoutBacklog = new Epic();
        epicWithoutBacklog.setId(epicId);

        when(epicRepositoryHelper.findByIdOrThrow(epicId)).thenReturn(epicWithoutBacklog);

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class,
                () -> epicService.getProductBacklogByEpicId(epicId));
        verify(epicRepositoryHelper).findByIdOrThrow(epicId);
    }

    @Test
    void testCountEpics() {
        // Setup
        when(epicRepository.count()).thenReturn(5L);

        // Execute
        long result = epicService.countEpics();

        // Verify
        assertEquals(5L, result);
        verify(epicRepository).count();
    }
}
