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

/**
 * Entité représentant une User Story dans la méthodologie Agile.
 * 
 * <p>
 * Une User Story est une description simple d'une fonctionnalité du point de
 * vue de l'utilisateur final.
 * Elle suit généralement le format : "En tant que [rôle], je veux
 * [fonctionnalité], afin de [bénéfice]".
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Décrit une fonctionnalité du point de vue utilisateur avec rôle, feature
 * et bénéfice</li>
 * <li>Possède une priorité pour le tri dans le backlog</li>
 * <li>Suit un cycle de vie avec différents statuts (TO DO, IN_PROGRESS, DONE,
 * etc.)</li>
 * <li>Peut appartenir à un Epic pour regroupement thématique</li>
 * <li>Peut être assignée à un Sprint Backlog pour planification</li>
 * <li>Contient des critères d'acceptation et peut être décomposée en
 * tâches</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>ProductBacklog:</b> Many-to-One - Une User Story appartient à un
 * Product Backlog</li>
 * <li><b>Epic:</b> Many-to-One (optionnel) - Une User Story peut appartenir à
 * un Epic</li>
 * <li><b>SprintBacklog:</b> Many-to-One (optionnel) - Une User Story peut être
 * assignée à un Sprint</li>
 * <li><b>AcceptanceCriteria:</b> One-to-Many - Une User Story possède plusieurs
 * critères d'acceptation</li>
 * <li><b>Tasks:</b> One-to-Many - Une User Story peut être décomposée en
 * plusieurs tâches</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see UserStoryStatus
 * @see Epic
 * @see ProductBacklog
 * @see SprintBacklog
 * @see AcceptanceCriteria
 * @see Task
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStory implements Serializable {

    /**
     * Identifiant unique de la User Story généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nom court de la User Story.
     * Doit contenir entre 3 et 100 caractères et ne peut pas être vide.
     */
    @NotBlank(message = NAME_BLANK_MESSAGE)
    @Size(min = NAME_MIN_LENGTH, max = NAME_MAX_LENGTH, message = NAME_SIZE_MESSAGE)
    private String name;

    /**
     * Rôle de l'utilisateur concerné par cette story (ex: "utilisateur",
     * "administrateur").
     * Format: "En tant que [role]..."
     */
    @NotBlank(message = ROLE_BLANK_MESSAGE)
    private String role;

    /**
     * Fonctionnalité désirée par l'utilisateur.
     * Format: "...je veux [feature]..."
     */
    @NotBlank(message = FEATURE_BLANK_MESSAGE)
    private String feature;

    /**
     * Bénéfice attendu de cette fonctionnalité.
     * Format: "...afin de [benefit]"
     */
    @NotBlank(message = BENEFIT_BLANK_MESSAGE)
    private String benefit;

    /**
     * Priorité de la User Story dans le backlog.
     * Valeur numérique utilisée pour le tri (plus petit = plus prioritaire).
     */
    @NotNull(message = PRIORITY_REQUIRED_MESSAGE)
    private int priority;

    /**
     * Statut actuel de la User Story dans son cycle de vie.
     * 
     * @see UserStoryStatus
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = STATUS_REQUIRED_MESSAGE)
    private UserStoryStatus status;

    /**
     * Product Backlog auquel appartient cette User Story.
     * Relation obligatoire, chargée en mode lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;

    /**
     * Epic auquel cette User Story est rattachée (optionnel).
     * Permet de regrouper plusieurs User Stories liées thématiquement.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "epic_id")
    private Epic epic;

    /**
     * Sprint Backlog auquel cette User Story est assignée (optionnel).
     * Null si la User Story n'est pas encore planifiée dans un sprint.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;

    /**
     * Liste des critères d'acceptation de cette User Story.
     * Supprimés en cascade lors de la suppression de la User Story.
     * Exclus de toString() et equals() pour éviter les références circulaires.
     */
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AcceptanceCriteria> acceptanceCriterias = new ArrayList<>();

    /**
     * Liste des tâches décomposées depuis cette User Story.
     * Supprimées en cascade lors de la suppression de la User Story.
     */
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();
}
