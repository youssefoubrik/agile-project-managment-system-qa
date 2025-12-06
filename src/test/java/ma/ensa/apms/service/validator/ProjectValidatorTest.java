package ma.ensa.apms.service.validator;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;

/**
 * Unit tests for {@link ProjectValidator}
 */
@DisplayName("ProjectValidator Tests")
class ProjectValidatorTest {

    private ProjectValidator projectValidator;

    @BeforeEach
    void setUp() {
        projectValidator = new ProjectValidator();
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when end date is null")
    void validateStartDate_WhenEndDateIsNull_ShouldNotThrowException() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();

        // When & Then
        assertThatCode(() -> projectValidator.validateStartDate(startDate, null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when start date is before end date")
    void validateStartDate_WhenStartDateIsBeforeEndDate_ShouldNotThrowException() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When & Then
        assertThatCode(() -> projectValidator.validateStartDate(startDate, endDate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateStartDate should throw exception when start date is after end date")
    void validateStartDate_WhenStartDateIsAfterEndDate_ShouldThrowException() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0);

        // When & Then
        assertThatThrownBy(() -> projectValidator.validateStartDate(startDate, endDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Start date must be before end date");
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when start date equals end date")
    void validateStartDate_WhenStartDateEqualsEndDate_ShouldNotThrowException() {
        // Given
        LocalDateTime sameDate = LocalDateTime.of(2024, 6, 15, 12, 0);

        // When & Then
        assertThatCode(() -> projectValidator.validateStartDate(sameDate, sameDate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when start date is null")
    void validateEndDate_WhenStartDateIsNull_ShouldNotThrowException() {
        // Given
        LocalDateTime endDate = LocalDateTime.now();

        // When & Then
        assertThatCode(() -> projectValidator.validateEndDate(null, endDate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when end date is after start date")
    void validateEndDate_WhenEndDateIsAfterStartDate_ShouldNotThrowException() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);

        // When & Then
        assertThatCode(() -> projectValidator.validateEndDate(startDate, endDate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateEndDate should throw exception when end date is before start date")
    void validateEndDate_WhenEndDateIsBeforeStartDate_ShouldThrowException() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 12, 31, 23, 59);
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 0, 0);

        // When & Then
        assertThatThrownBy(() -> projectValidator.validateEndDate(startDate, endDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("End date must be after start date");
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when end date equals start date")
    void validateEndDate_WhenEndDateEqualsStartDate_ShouldNotThrowException() {
        // Given
        LocalDateTime sameDate = LocalDateTime.of(2024, 6, 15, 12, 0);

        // When & Then
        assertThatCode(() -> projectValidator.validateEndDate(sameDate, sameDate))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateProductBacklogAssignment should not throw exception when product backlog is null")
    void validateProductBacklogAssignment_WhenProductBacklogIsNull_ShouldNotThrowException() {
        // Given
        Project project = new Project();
        project.setProductBacklog(null);

        // When & Then
        assertThatCode(() -> projectValidator.validateProductBacklogAssignment(project))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("validateProductBacklogAssignment should throw exception when product backlog is already assigned")
    void validateProductBacklogAssignment_WhenProductBacklogIsAssigned_ShouldThrowException() {
        // Given
        Project project = new Project();
        ProductBacklog productBacklog = new ProductBacklog();
        project.setProductBacklog(productBacklog);

        // When & Then
        assertThatThrownBy(() -> projectValidator.validateProductBacklogAssignment(project))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("This project already has a ProductBacklog assigned");
    }

    @Test
    @DisplayName("validateStartDate should handle dates with same day but different times")
    void validateStartDate_WithSameDayDifferentTimes_ShouldValidateCorrectly() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 15, 14, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 10, 0);

        // When & Then
        assertThatThrownBy(() -> projectValidator.validateStartDate(startDate, endDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Start date must be before end date");
    }

    @Test
    @DisplayName("validateEndDate should handle dates with same day but different times")
    void validateEndDate_WithSameDayDifferentTimes_ShouldValidateCorrectly() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2024, 6, 15, 14, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 6, 15, 10, 0);

        // When & Then
        assertThatThrownBy(() -> projectValidator.validateEndDate(startDate, endDate))
                .isInstanceOf(BusinessException.class)
                .hasMessage("End date must be after start date");
    }
}
