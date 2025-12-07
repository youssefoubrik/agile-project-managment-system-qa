package ma.ensa.apms.modal;

import static ma.ensa.apms.validation.ValidationConstants.Task.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensa.apms.modal.enums.TaskStatus;
import ma.ensa.apms.validation.DateRangeHolder;
import ma.ensa.apms.validation.StartEndDateValidator;

/**
 * Entité représentant une tâche technique dans le système de gestion de projet
 * Agile.
 * 
 * <p>
 * Une tâche est une unité de travail atomique qui décompose une User Story en
 * actions
 * concrètes et réalisables. Elle représente le niveau de granularité le plus
 * fin dans
 * la hiérarchie du projet (Projet → Epic → User Story → Task).
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Possède un titre (3-100 caractères) et une description détaillée</li>
 * <li>Suit un cycle de vie avec différents statuts (TO DO, IN_PROGRESS, DONE,
 * etc.)</li>
 * <li>Définit une période avec dates de début et fin</li>
 * <li>Appartient toujours à une User Story parent</li>
 * <li>Validation automatique que la date de fin est postérieure à la date de
 * début</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>UserStory:</b> Many-to-One - Une tâche appartient à une User
 * Story</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see TaskStatus
 * @see UserStory
 * @see DateRangeHolder
 * @see StartEndDateValidator
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@StartEndDateValidator
public class Task implements Serializable, DateRangeHolder {

    /**
     * Identifiant unique de la tâche généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Titre de la tâche.
     * Doit contenir entre 3 et 100 caractères et ne peut pas être vide.
     */
    @NotBlank(message = TITLE_BLANK_MESSAGE)
    @Size(min = TITLE_MIN_LENGTH, max = TITLE_MAX_LENGTH, message = TITLE_SIZE_MESSAGE)
    private String title;

    /**
     * Description détaillée de la tâche.
     * Ce champ est obligatoire et ne peut pas être vide.
     */
    @NotBlank(message = DESCRIPTION_BLANK_MESSAGE)
    private String description;

    /**
     * Statut actuel de la tâche dans son cycle de vie.
     * 
     * @see TaskStatus
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private TaskStatus status;

    /**
     * Date et heure de début prévues ou effectives de la tâche.
     */
    private LocalDateTime startDate;

    /**
     * Date et heure de fin prévues ou effectives de la tâche.
     * Doit être postérieure à la date de début (validation via
     * {@link StartEndDateValidator}).
     */
    private LocalDateTime endDate;

    /**
     * User Story parent à laquelle cette tâche appartient.
     * Relation obligatoire, chargée en mode lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;
}
