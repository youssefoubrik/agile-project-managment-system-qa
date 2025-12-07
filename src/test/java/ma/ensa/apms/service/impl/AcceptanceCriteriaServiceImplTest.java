package ma.ensa.apms.service.impl;

import ma.ensa.apms.dto.Request.AcceptanceCriteriaRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.mapper.AcceptanceCriteriaMapper;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;
import ma.ensa.apms.service.helper.AcceptanceCriteriaRepositoryHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptanceCriteriaServiceImplTest {

    @Mock
    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;

    @Mock
    private AcceptanceCriteriaMapper acceptanceCriteriaMapper;

    @Mock
    private AcceptanceCriteriaRepositoryHelper acceptanceCriteriaRepositoryHelper;

    @InjectMocks
    private AcceptanceCriteriaServiceImpl acceptanceCriteriaService;

    private UUID id;
    private AcceptanceCriteriaRequest requestDto;
    private AcceptanceCriteria entity;
    private AcceptanceCriteriaResponse responseDto;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();

        requestDto = AcceptanceCriteriaRequest.builder()
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();

        entity = AcceptanceCriteria.builder()
                .id(id)
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();

        responseDto = AcceptanceCriteriaResponse.builder()
                .id(id)
                .given("Given a user is logged in")
                .when("When the user clicks on the logout button")
                .then("Then the user is logged out")
                .met(false)
                .build();
    }

    @Test
    void testCreate() {
        // Arrange
        when(acceptanceCriteriaMapper.toEntity(requestDto)).thenReturn(entity);
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.create(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaMapper).toEntity(requestDto);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindById_ExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.findById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindById_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id))
                .thenThrow(new ResourceNotFoundException("AcceptanceCriteria not found with id: " + id));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.findById(id));
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
    }

    @Test
    void testFindAllByMet() {
        // Arrange
        boolean metStatus = true;
        List<AcceptanceCriteria> entities = Arrays.asList(entity);
        when(acceptanceCriteriaRepository.findByMet(metStatus)).thenReturn(entities);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        List<AcceptanceCriteriaResponse> results = acceptanceCriteriaService.findAllByMet(metStatus);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(acceptanceCriteriaRepository).findByMet(metStatus);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<AcceptanceCriteria> entities = Arrays.asList(entity);
        when(acceptanceCriteriaRepository.findAll()).thenReturn(entities);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        List<AcceptanceCriteriaResponse> results = acceptanceCriteriaService.findAll();

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        verify(acceptanceCriteriaRepository).findAll();
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdate_ExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id)).thenReturn(entity);
        doNothing().when(acceptanceCriteriaMapper).updateEntityFromDto(requestDto, entity);
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.update(id, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
        verify(acceptanceCriteriaMapper).updateEntityFromDto(requestDto, entity);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdate_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id))
                .thenThrow(new ResourceNotFoundException("AcceptanceCriteria not found with id: " + id));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.update(id, requestDto));
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
        verify(acceptanceCriteriaMapper, never()).updateEntityFromDto(any(), any());
    }

    @Test
    void testDelete_ExistingId() {
        // Arrange
        doNothing().when(acceptanceCriteriaRepositoryHelper).validateExists(id);
        doNothing().when(acceptanceCriteriaRepository).deleteById(id);

        // Act
        acceptanceCriteriaService.delete(id);

        // Assert
        verify(acceptanceCriteriaRepositoryHelper).validateExists(id);
        verify(acceptanceCriteriaRepository).deleteById(id);
    }

    @Test
    void testDelete_NonExistingId() {
        // Arrange
        doThrow(new ResourceNotFoundException("AcceptanceCriteria not found with id: " + id))
                .when(acceptanceCriteriaRepositoryHelper).validateExists(id);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.delete(id));
        verify(acceptanceCriteriaRepositoryHelper).validateExists(id);
        verify(acceptanceCriteriaRepository, never()).deleteById(any());
    }

    @Test
    void testUpdateMet_ExistingId() {
        // Arrange
        boolean newMetStatus = true;
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id)).thenReturn(entity);
        when(acceptanceCriteriaRepository.save(entity)).thenReturn(entity);
        when(acceptanceCriteriaMapper.toDto(entity)).thenReturn(responseDto);

        // Act
        AcceptanceCriteriaResponse result = acceptanceCriteriaService.updateMet(id, newMetStatus);

        // Assert
        assertNotNull(result);
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
        verify(acceptanceCriteriaRepository).save(entity);
        verify(acceptanceCriteriaMapper).toDto(entity);
    }

    @Test
    void testUpdateMet_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id))
                .thenThrow(new ResourceNotFoundException("AcceptanceCriteria not found with id: " + id));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> acceptanceCriteriaService.updateMet(id, true));
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
        verify(acceptanceCriteriaRepository, never()).save(any());
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_ExistingIdWithUserStory() {
        // Arrange
        UserStory userStory = new UserStory();
        entity.setUserStory(userStory);

        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id)).thenReturn(entity);

        // Act
        UserStoryResponse result = acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id);

        // Assert
        assertNotNull(result);
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_ExistingIdWithoutUserStory() {
        // Arrange
        entity.setUserStory(null);
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id)).thenReturn(entity);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id));
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
    }

    @Test
    void testGetUserStoryByAcceptanceCriteriaId_NonExistingId() {
        // Arrange
        when(acceptanceCriteriaRepositoryHelper.findByIdOrThrow(id))
                .thenThrow(new ResourceNotFoundException("AcceptanceCriteria not found with id: " + id));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> acceptanceCriteriaService.getUserStoryByAcceptanceCriteriaId(id));
        verify(acceptanceCriteriaRepositoryHelper).findByIdOrThrow(id);
    }
}
