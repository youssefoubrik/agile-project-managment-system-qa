package ma.ensa.apms.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Test
    @DisplayName("isValid should return true for valid enum value")
    void isValid_WithValidEnumValue_ShouldReturnTrue() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("VALUE_ONE", context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true for valid enum value with different case")
    void isValid_WithValidEnumValueDifferentCase_ShouldReturnTrue() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("value_one", context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true for valid enum value in uppercase")
    void isValid_WithValidEnumValueUpperCase_ShouldReturnTrue() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("VALUE_TWO", context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true for valid enum value in lowercase")
    void isValid_WithValidEnumValueLowerCase_ShouldReturnTrue() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("value_three", context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return true for valid enum value in mixed case")
    void isValid_WithValidEnumValueMixedCase_ShouldReturnTrue() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("VaLuE_OnE", context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("isValid should return false for invalid enum value")
    void isValid_WithInvalidEnumValue_ShouldReturnFalse() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("INVALID_VALUE", context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return false for null value")
    void isValid_WithNullValue_ShouldReturnFalse() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid(null, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return false for empty string")
    void isValid_WithEmptyString_ShouldReturnFalse() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("", context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return false for blank string")
    void isValid_WithBlankString_ShouldReturnFalse() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("   ", context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("isValid should return false for partial enum value match")
    void isValid_WithPartialEnumValueMatch_ShouldReturnFalse() {
        // Given
        initializeValidator();

        // When
        boolean result = validator.isValid("VALUE", context);

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
