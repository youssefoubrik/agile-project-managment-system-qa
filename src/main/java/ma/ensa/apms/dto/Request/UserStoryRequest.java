package ma.ensa.apms.dto.Request;

import static ma.ensa.apms.validation.ValidationConstants.UserStory.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStoryRequest {
    @NotBlank(message = NAME_BLANK_MESSAGE)
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_SIZE_MESSAGE)
    private String name;

    @NotBlank(message = ROLE_BLANK_MESSAGE)
    private String role;

    @NotBlank(message = FEATURE_BLANK_MESSAGE)
    private String feature;

    @NotBlank(message = BENEFIT_BLANK_MESSAGE)
    private String benefit;

    @NotNull(message = PRIORITY_REQUIRED_MESSAGE)
    private int priority;

    @NotNull(message = STATUS_REQUIRED_MESSAGE)
    private UserStoryStatus status;
}
