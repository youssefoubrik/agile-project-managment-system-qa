package ma.ensa.apms.service.helper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.repository.ProductBacklogRepository;

/**
 * Unit tests for {@link ProductBacklogRepositoryHelper}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductBacklogRepositoryHelper Tests")
class ProductBacklogRepositoryHelperTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private ProductBacklogRepositoryHelper productBacklogRepositoryHelper;

    private UUID productBacklogId;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        productBacklogId = UUID.randomUUID();
        productBacklog = new ProductBacklog();
        productBacklog.setId(productBacklogId);
        productBacklog.setName("Test Product Backlog");
    }

    @Test
    @DisplayName("findByIdOrThrow should return product backlog when found")
    void findByIdOrThrow_WhenProductBacklogExists_ShouldReturnProductBacklog() {
        // Given
        when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.of(productBacklog));

        // When
        ProductBacklog result = productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productBacklogId);
        assertThat(result.getName()).isEqualTo("Test Product Backlog");
        verify(productBacklogRepository).findById(productBacklogId);
    }

    @Test
    @DisplayName("findByIdOrThrow should throw ResourceNotFoundException when product backlog not found")
    void findByIdOrThrow_WhenProductBacklogNotFound_ShouldThrowException() {
        // Given
        when(productBacklogRepository.findById(productBacklogId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productBacklogRepositoryHelper.findByIdOrThrow(productBacklogId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product backlog not found");
        verify(productBacklogRepository).findById(productBacklogId);
    }

    @Test
    @DisplayName("validateExists should not throw exception when product backlog exists")
    void validateExists_WhenProductBacklogExists_ShouldNotThrowException() {
        // Given
        when(productBacklogRepository.existsById(productBacklogId)).thenReturn(true);

        // When & Then
        assertThatCode(() -> productBacklogRepositoryHelper.validateExists(productBacklogId))
                .doesNotThrowAnyException();
        verify(productBacklogRepository).existsById(productBacklogId);
    }

    @Test
    @DisplayName("validateExists should throw ResourceNotFoundException when product backlog does not exist")
    void validateExists_WhenProductBacklogDoesNotExist_ShouldThrowException() {
        // Given
        when(productBacklogRepository.existsById(productBacklogId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> productBacklogRepositoryHelper.validateExists(productBacklogId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product backlog not found");
        verify(productBacklogRepository).existsById(productBacklogId);
    }

    @Test
    @DisplayName("findByIdOrThrow should work with different product backlog IDs")
    void findByIdOrThrow_WithDifferentIds_ShouldWorkCorrectly() {
        // Given
        UUID anotherId = UUID.randomUUID();
        ProductBacklog anotherBacklog = new ProductBacklog();
        anotherBacklog.setId(anotherId);
        anotherBacklog.setName("Another Product Backlog");
        when(productBacklogRepository.findById(anotherId)).thenReturn(Optional.of(anotherBacklog));

        // When
        ProductBacklog result = productBacklogRepositoryHelper.findByIdOrThrow(anotherId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(anotherId);
        assertThat(result.getName()).isEqualTo("Another Product Backlog");
        verify(productBacklogRepository).findById(anotherId);
    }
}
