package ma.ensa.apms.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StartEndDateConstraintValidator}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StartEndDateConstraintValidator Tests")
class StartEndDateConstraintValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private StartEndDateConstraintValidator validator;

    @BeforeEach
    void setUp() {
        validator = new StartEndDateConstraintValidator();
    }

    @Test
    @DisplayName("isValid should return true when start date is before end date")
    void isValid_WhenStartDateIsBeforeEndDate_ShouldReturnTrue() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2025, 12, 31, 18, 0));

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true when both dates are null")
    void isValid_WhenBothDatesAreNull_ShouldReturnTrue() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(null, null);

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true when start date is null")
    void isValid_WhenStartDateIsNull_ShouldReturnTrue() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(
                null,
                LocalDateTime.of(2025, 12, 31, 18, 0));

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true when end date is null")
    void isValid_WhenEndDateIsNull_ShouldReturnTrue() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(
                LocalDateTime.of(2025, 1, 1, 10, 0),
                null);

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return false when start date is after end date")
    void isValid_WhenStartDateIsAfterEndDate_ShouldReturnFalse() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(
                LocalDateTime.of(2025, 12, 31, 18, 0),
                LocalDateTime.of(2025, 1, 1, 10, 0));

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return false when start date equals end date")
    void isValid_WhenStartDateEqualsEndDate_ShouldReturnFalse() {
        // Given
        LocalDateTime sameDate = LocalDateTime.of(2025, 6, 15, 12, 0);
        TestDateRangeHolder holder = new TestDateRangeHolder(sameDate, sameDate);

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return true when dates are one second apart")
    void isValid_WhenDatesAreOneSecondApart_ShouldReturnTrue() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 15, 12, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 15, 12, 0, 1);
        TestDateRangeHolder holder = new TestDateRangeHolder(startDate, endDate);

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true when end date is far in the future")
    void isValid_WhenEndDateIsFarInTheFuture_ShouldReturnTrue() {
        // Given
        TestDateRangeHolder holder = new TestDateRangeHolder(
                LocalDateTime.of(2025, 1, 1, 10, 0),
                LocalDateTime.of(2050, 12, 31, 18, 0));

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return false when end date is one second before start date")
    void isValid_WhenEndDateIsOneSecondBeforeStartDate_ShouldReturnFalse() {
        // Given
        LocalDateTime startDate = LocalDateTime.of(2025, 6, 15, 12, 0, 1);
        LocalDateTime endDate = LocalDateTime.of(2025, 6, 15, 12, 0, 0);
        TestDateRangeHolder holder = new TestDateRangeHolder(startDate, endDate);

        // When
        boolean result = validator.isValid(holder, context);

        // Then
        assertThat(result).isFalse();
    }

    /**
     * Test implementation of DateRangeHolder for testing purposes
     */
    private static class TestDateRangeHolder implements DateRangeHolder {
        private final LocalDateTime startDate;
        private final LocalDateTime endDate;

        public TestDateRangeHolder(LocalDateTime startDate, LocalDateTime endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        @Override
        public LocalDateTime getStartDate() {
            return startDate;
        }

        @Override
        public LocalDateTime getEndDate() {
            return endDate;
        }
    }
}
