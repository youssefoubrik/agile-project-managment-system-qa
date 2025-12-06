package ma.ensa.apms.modal;

import static ma.ensa.apms.validation.ValidationConstants.UserStory.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.ensa.apms.modal.enums.UserStoryStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

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

    @Enumerated(EnumType.STRING)
    @NotNull(message = STATUS_REQUIRED_MESSAGE)
    private UserStoryStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id")
    private Epic epic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AcceptanceCriteria> acceptanceCriterias = new ArrayList<>();

    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();
}
