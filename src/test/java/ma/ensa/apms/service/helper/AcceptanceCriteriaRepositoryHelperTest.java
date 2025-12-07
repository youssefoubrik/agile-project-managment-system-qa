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
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;

/**
 * Unit tests for AcceptanceCriteriaRepositoryHelper.
 * 
 * Tests the helper methods for AcceptanceCriteria repository operations,
 * including retrieval and validation logic.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AcceptanceCriteriaRepositoryHelper Tests")
class AcceptanceCriteriaRepositoryHelperTest {

    @Mock
    private AcceptanceCriteriaRepository acceptanceCriteriaRepository;

    @InjectMocks
    private AcceptanceCriteriaRepositoryHelper acceptanceCriteriaRepositoryHelper;

    private UUID acceptanceCriteriaId;
    private AcceptanceCriteria acceptanceCriteria;

    @BeforeEach
    void setUp() {
        acceptanceCriteriaId = UUID.randomUUID();
        acceptanceCriteria = new AcceptanceCriteria();
        acceptanceCriteria.setId(acceptanceCriteriaId);
    }

    @Test
    @DisplayName("findByIdOrThrow should return acceptance criteria when it exists")
    void findByIdOrThrow_ShouldReturnAcceptanceCriteria_WhenAcceptanceCriteriaExists() {
        // Given
        when(acceptanceCriteriaRepository.findById(acceptanceCriteriaId))
                .thenReturn(Optional.of(acceptanceCriteria));

        // When
        AcceptanceCriteria result = acceptanceCriteriaRepositoryHelper.findByIdOrThrow(acceptanceCriteriaId);

        // Then
        assertNotNull(result);
        assertEquals(acceptanceCriteriaId, result.getId());
        verify(acceptanceCriteriaRepository).findById(acceptanceCriteriaId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw exception when acceptance criteria does not exist")
    void findByIdOrThrow_ShouldThrowException_WhenAcceptanceCriteriaDoesNotExist() {
        // Given
        when(acceptanceCriteriaRepository.findById(acceptanceCriteriaId))
                .thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> acceptanceCriteriaRepositoryHelper.findByIdOrThrow(acceptanceCriteriaId));

        assertTrue(exception.getMessage().contains("Acceptance Criteria not found"));
        verify(acceptanceCriteriaRepository).findById(acceptanceCriteriaId);
    }

    @Test
    @DisplayName("validateExists should not throw exception when acceptance criteria exists")
    void validateExists_ShouldNotThrowException_WhenAcceptanceCriteriaExists() {
        // Given
        when(acceptanceCriteriaRepository.existsById(acceptanceCriteriaId)).thenReturn(true);

        // When & Then
        assertDoesNotThrow(() -> acceptanceCriteriaRepositoryHelper.validateExists(acceptanceCriteriaId));
        verify(acceptanceCriteriaRepository).existsById(acceptanceCriteriaId);
    }

    @Test
    @DisplayName("validateExists should throw exception when acceptance criteria does not exist")
    void validateExists_ShouldThrowException_WhenAcceptanceCriteriaDoesNotExist() {
        // Given
        when(acceptanceCriteriaRepository.existsById(acceptanceCriteriaId)).thenReturn(false);

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> acceptanceCriteriaRepositoryHelper.validateExists(acceptanceCriteriaId));

        assertTrue(exception.getMessage().contains("Acceptance Criteria not found"));
        verify(acceptanceCriteriaRepository).existsById(acceptanceCriteriaId);
    }
}
