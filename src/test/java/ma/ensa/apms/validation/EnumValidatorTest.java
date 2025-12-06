package ma.ensa.apms.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EnumValidator}
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("EnumValidator Tests")
class EnumValidatorTest {

    @Mock
    private ConstraintValidatorContext context;

    private EnumValidator validator;

    // Test enum for validation
    enum TestEnum {
        VALUE_ONE,
        VALUE_TWO,
        VALUE_THREE
    }

    @BeforeEach
    void setUp() {
        validator = new EnumValidator();
    }

    private void initializeValidator() {
        EnumValid annotation = new EnumValid() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return EnumValid.class;
            }

            @Override
            public Class<? extends Enum<?>> enumClass() {
                return TestEnum.class;
            }

            @Override
            public String message() {
                return "Invalid value";
            }

            @Override
            public Class<?>[] groups() {
                return new Class[0];
            }

            @Override
            public Class<? extends jakarta.validation.Payload>[] payload() {
                return new Class[0];
            }
        };
        validator.initialize(annotation);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "VALUE_ONE",
            "value_one",
            "VALUE_TWO",
            "value_three",
            "VaLuE_OnE"
    })
    @DisplayName("isValid should return true for valid enum values with different cases")
    void isValid_WithValidEnumValueDifferentCases_ShouldReturnTrue(String value) {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid(value, context);

        // Then
        assertThat(result).isTrue();
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {
            "INVALID_VALUE",
            "   ",
            "VALUE"
    })
    @DisplayName("isValid should return false for invalid enum values")
    void isValid_WithInvalidEnumValues_ShouldReturnFalse(String value) {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid(value, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should work correctly with all enum constants")
    void isValid_ShouldWorkCorrectlyWithAllEnumConstants() {
        // Given
        initializeValidator();

        // When & Then
        assertThat(validator.isValid("VALUE_ONE", context)).isTrue();
        assertThat(validator.isValid("VALUE_TWO", context)).isTrue();
        assertThat(validator.isValid("VALUE_THREE", context)).isTrue();
        assertThat(validator.isValid("VALUE_FOUR", context)).isFalse();
    }
}
