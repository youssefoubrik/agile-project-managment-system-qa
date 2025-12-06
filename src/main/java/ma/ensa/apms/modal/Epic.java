package ma.ensa.apms.modal;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entité représentant un Epic dans la méthodologie Agile.
 * 
 * <p>
 * Un Epic est une grande fonctionnalité ou initiative qui regroupe plusieurs
 * User Stories liées.
 * Il s'agit d'un élément de haut niveau du Product Backlog qui est trop
 * volumineux pour être
 * complété dans un seul sprint et qui doit être décomposé en User Stories plus
 * petites.
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Regroupe plusieurs User Stories thématiquement liées</li>
 * <li>Appartient à un Product Backlog</li>
 * <li>Possède un nom descriptif (3-100 caractères) et une description détaillée
 * (max 1000 caractères)</li>
 * <li>Les User Stories associées sont supprimées en cascade lors de la
 * suppression de l'Epic</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>ProductBacklog:</b> Many-to-One - Un Epic appartient à un Product
 * Backlog</li>
 * <li><b>UserStories:</b> One-to-Many - Un Epic contient plusieurs User
 * Stories</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see BaseEntity
 * @see UserStory
 * @see ProductBacklog
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Epic extends BaseEntity {
    /**
     * Identifiant unique de l'Epic généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nom de l'Epic. Doit contenir entre 3 et 100 caractères.
     * Ce champ est obligatoire et ne peut pas être vide.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    /**
     * Description détaillée de l'Epic (optionnelle).
     * Peut contenir jusqu'à 1000 caractères et est stockée comme TEXT en base de
     * données.
     */
    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    /**
     * Liste des User Stories appartenant à cet Epic.
     * Les User Stories sont supprimées en cascade lors de la suppression de l'Epic.
     * Exclue des méthodes toString() et equals() pour éviter les références
     * circulaires.
     */
    @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<UserStory> userStories;

    /**
     * Product Backlog auquel appartient cet Epic.
     * Chargé en mode lazy pour optimiser les performances.
     * Exclu des méthodes toString() et equals() pour éviter les références
     * circulaires.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_backlog_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ProductBacklog productBacklog;
}
