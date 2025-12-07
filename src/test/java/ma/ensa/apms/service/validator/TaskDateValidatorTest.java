package ma.ensa.apms.service.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ma.ensa.apms.exception.BusinessException;

/**
 * Unit tests for TaskDateValidator.
 * 
 * Tests the validation logic for Task date fields,
 * ensuring business rules are properly enforced.
 */
@DisplayName("TaskDateValidator Tests")
class TaskDateValidatorTest {

    private TaskDateValidator taskDateValidator;

    @BeforeEach
    void setUp() {
        taskDateValidator = new TaskDateValidator();
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when start date is before end date")
    void validateStartDate_ShouldNotThrowException_WhenStartDateIsBeforeEndDate() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(5);

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateStartDate(startDate, endDate));
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when end date is null")
    void validateStartDate_ShouldNotThrowException_WhenEndDateIsNull() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateStartDate(startDate, null));
    }

    @Test
    @DisplayName("validateStartDate should throw exception when start date is after end date")
    void validateStartDate_ShouldThrowException_WhenStartDateIsAfterEndDate() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusDays(5);

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> taskDateValidator.validateStartDate(startDate, endDate));

        assertEquals("Start date cannot be after the end date", exception.getMessage());
    }

    @Test
    @DisplayName("validateStartDate should not throw exception when dates are equal")
    void validateStartDate_ShouldNotThrowException_WhenDatesAreEqual() {
        // Given
        LocalDateTime date = LocalDateTime.now();

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateStartDate(date, date));
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when end date is after start date")
    void validateEndDate_ShouldNotThrowException_WhenEndDateIsAfterStartDate() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.plusDays(5);

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateEndDate(startDate, endDate));
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when start date is null")
    void validateEndDate_ShouldNotThrowException_WhenStartDateIsNull() {
        // Given
        LocalDateTime endDate = LocalDateTime.now();

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateEndDate(null, endDate));
    }

    @Test
    @DisplayName("validateEndDate should throw exception when end date is before start date")
    void validateEndDate_ShouldThrowException_WhenEndDateIsBeforeStartDate() {
        // Given
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = startDate.minusDays(5);

        // When & Then
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> taskDateValidator.validateEndDate(startDate, endDate));

        assertEquals("End date cannot be before the start date", exception.getMessage());
    }

    @Test
    @DisplayName("validateEndDate should not throw exception when dates are equal")
    void validateEndDate_ShouldNotThrowException_WhenDatesAreEqual() {
        // Given
        LocalDateTime date = LocalDateTime.now();

        // When & Then
        assertDoesNotThrow(() -> taskDateValidator.validateEndDate(date, date));
    }
}
