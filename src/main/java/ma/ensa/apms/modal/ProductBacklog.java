package ma.ensa.apms.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * Entité représentant le Product Backlog dans la méthodologie Agile/Scrum.
 * 
 * <p>
 * Le Product Backlog est la liste ordonnée et évolutive de tout ce qui pourrait
 * être nécessaire
 * dans le produit. C'est la source unique des exigences pour toutes les
 * modifications à apporter au produit.
 * Il contient toutes les User Stories, Epics et organise les Sprints du projet.
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Point central de gestion de toutes les exigences du produit</li>
 * <li>Appartient à un seul projet (relation bidirectionnelle one-to-one)</li>
 * <li>Contient tous les Epics et User Stories du projet</li>
 * <li>Gère les Sprint Backlogs pour la planification des itérations</li>
 * <li>Tous les éléments enfants sont supprimés en cascade</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>Project:</b> One-to-One - Un Product Backlog appartient à un projet
 * unique</li>
 * <li><b>UserStories:</b> One-to-Many - Contient toutes les User Stories du
 * projet</li>
 * <li><b>Epics:</b> One-to-Many - Contient tous les Epics du projet</li>
 * <li><b>SprintBacklogs:</b> One-to-Many - Gère tous les sprints du projet</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see BaseEntity
 * @see Project
 * @see UserStory
 * @see Epic
 * @see SprintBacklog
 */
@Entity
@Table(name = "product_backlog")
@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductBacklog extends BaseEntity {
    /**
     * Identifiant unique du Product Backlog généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nom du Product Backlog. Ce champ est obligatoire.
     */
    @NotNull
    private String name;

    /**
     * Projet auquel appartient ce Product Backlog.
     * Relation bidirectionnelle one-to-one.
     * Exclu de toString() et equals() pour éviter les références circulaires.
     */
    @OneToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Project project;

    /**
     * Liste de toutes les User Stories du Product Backlog.
     * Supprimées en cascade lors de la suppression du Product Backlog.
     * Exclues de toString() et equals() pour éviter les références circulaires.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserStory> userStories;

    /**
     * Liste de tous les Epics du Product Backlog.
     * Supprimés en cascade lors de la suppression du Product Backlog.
     * Exclus de toString() et equals() pour éviter les références circulaires.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Epic> epics;

    /**
     * Liste de tous les Sprint Backlogs du Product Backlog.
     * Supprimés en cascade lors de la suppression du Product Backlog.
     * Exclus de toString() et equals() pour éviter les références circulaires.
     */
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SprintBacklog> sprintBacklogs;
}
