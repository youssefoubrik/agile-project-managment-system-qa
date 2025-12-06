package ma.ensa.apms.service.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;

/**
 * Unit tests for {@link ProductBacklogValidator}
 */
@DisplayName("ProductBacklogValidator Tests")
class ProductBacklogValidatorTest {

    private ProductBacklogValidator productBacklogValidator;

    @BeforeEach
    void setUp() {
        productBacklogValidator = new ProductBacklogValidator();
    }

    @Test
    @DisplayName("validateHasProject should not throw exception when product backlog has a project")
    void validateHasProject_WhenProjectExists_ShouldNotThrowException() {
        // Given
        ProductBacklog productBacklog = new ProductBacklog();
        Project project = new Project();
        project.setName("Test Project");
        productBacklog.setProject(project);

        // When & Then
        assertThatCode(() -> productBacklogValidator.validateHasProject(productBacklog))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateHasProject should throw exception when product backlog has no project")
    void validateHasProject_WhenProjectIsNull_ShouldThrowException() {
        // Given
        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setProject(null);

        // When & Then
        assertThatThrownBy(() -> productBacklogValidator.validateHasProject(productBacklog))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("No project associated with this product backlog");
    }

    @Test
    @DisplayName("validateHasProject should work with different projects")
    void validateHasProject_WithDifferentProjects_ShouldWorkCorrectly() {
        // Given
        ProductBacklog productBacklog = new ProductBacklog();
        Project project = new Project();
        project.setName("Another Project");
        productBacklog.setProject(project);

        // When & Then
        assertThatCode(() -> productBacklogValidator.validateHasProject(productBacklog))
                .doesNotThrowAnyException();
    }
}
