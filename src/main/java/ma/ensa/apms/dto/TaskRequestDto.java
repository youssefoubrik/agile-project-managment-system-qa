package ma.ensa.apms.dto;

import static ma.ensa.apms.validation.ValidationConstants.Task.*;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ma.ensa.apms.modal.enums.TaskStatus;

@Data
public class TaskRequestDto {
    @NotBlank(message = TITLE_BLANK_MESSAGE)
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_SIZE_MESSAGE)
    private String title;
    @NotBlank(message = DESCRIPTION_BLANK_MESSAGE)
    private String description;
    private TaskStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
