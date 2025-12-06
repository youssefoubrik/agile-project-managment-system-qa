package ma.ensa.apms.modal;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entité représentant un Sprint Backlog dans la méthodologie Agile/Scrum.
 * 
 * <p>
 * Le Sprint Backlog est l'ensemble des User Stories sélectionnées depuis le
 * Product Backlog
 * pour être réalisées durant un sprint spécifique, ainsi que le plan pour
 * livrer l'incrément
 * du produit et atteindre l'objectif du sprint.
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Représente le travail planifié pour une itération (sprint)</li>
 * <li>Possède un nom unique (3-50 caractères)</li>
 * <li>Contient les User Stories assignées au sprint</li>
 * <li>Appartient à un Product Backlog parent</li>
 * <li>Permet de suivre la progression du sprint</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>ProductBacklog:</b> Many-to-One - Un Sprint Backlog appartient à un
 * Product Backlog</li>
 * <li><b>UserStories:</b> One-to-Many - Contient les User Stories assignées à
 * ce sprint</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see BaseEntity
 * @see ProductBacklog
 * @see UserStory
 */
@Entity
@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SprintBacklog extends BaseEntity {
    /**
     * Identifiant unique du Sprint Backlog généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nom du Sprint Backlog.
     * Doit contenir entre 3 et 50 caractères et ne peut pas être null.
     * Exemple: "Sprint 1", "Sprint Planning Q1 2025"
     */
    @NotNull(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    /**
     * Liste des User Stories assignées à ce Sprint Backlog.
     * Ces User Stories sont sélectionnées depuis le Product Backlog parent.
     */
    @OneToMany(mappedBy = "sprintBacklog")
    private List<UserStory> userStories;

    /**
     * Product Backlog parent auquel appartient ce Sprint Backlog.
     * Un Sprint Backlog est toujours lié à un Product Backlog.
     */
    @ManyToOne
    private ProductBacklog productBacklog;
}
