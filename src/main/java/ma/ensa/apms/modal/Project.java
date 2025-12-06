package ma.ensa.apms.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ma.ensa.apms.modal.enums.ProjectStatus;
import ma.ensa.apms.validation.DateRangeHolder;
import ma.ensa.apms.validation.StartEndDateValidator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entité représentant un projet dans le système de gestion de projet Agile.
 * 
 * <p>
 * Un projet est l'entité principale qui encapsule tous les éléments d'un projet
 * Agile.
 * Chaque projet possède un Product Backlog qui contient les Epics et User
 * Stories.
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Gère le cycle de vie complet d'un projet Agile</li>
 * <li>Possède un statut qui suit l'évolution du projet</li>
 * <li>Définit une période avec dates de début et fin</li>
 * <li>Est lié à un Product Backlog unique</li>
 * <li>Validation automatique que la date de fin est postérieure à la date de
 * début</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ProjectStatus
 * @see ProductBacklog
 * @see BaseEntity
 */
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@StartEndDateValidator
public class Project extends BaseEntity implements Serializable, DateRangeHolder {

    /**
     * Identifiant unique du projet généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nom du projet. Ce champ est obligatoire et ne peut pas être vide.
     */
    @NotBlank(message = "Name is required")
    private String name;

    /**
     * Description détaillée du projet (optionnelle).
     */
    private String description;

    /**
     * Product Backlog associé au projet.
     * Relation one-to-one : un projet possède un unique Product Backlog.
     */
    @OneToOne
    private ProductBacklog productBacklog;

    /**
     * Statut actuel du projet (ACTIVE, COMPLETED, etc.).
     * 
     * @see ProjectStatus
     */
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    /**
     * Date et heure de début prévues du projet.
     */
    private LocalDateTime startDate;

    /**
     * Date et heure de fin prévues du projet.
     * Doit être postérieure à la date de début (validation via
     * {@link StartEndDateValidator}).
     */
    private LocalDateTime endDate;
}
