package ma.ensa.apms.modal;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

/**
 * Entité représentant un critère d'acceptation pour une User Story.
 * 
 * <p>
 * Un critère d'acceptation définit les conditions qui doivent être satisfaites
 * pour qu'une
 * User Story soit considérée comme "Done" (terminée). Il suit le format BDD
 * (Behavior-Driven Development) :
 * "GIVEN [condition initiale] WHEN [action] THEN [résultat attendu]".
 * </p>
 * 
 * <p>
 * <b>Format BDD:</b>
 * </p>
 * <ul>
 * <li><b>GIVEN (Étant donné):</b> Décrit le contexte ou l'état initial</li>
 * <li><b>WHEN (Quand):</b> Décrit l'action ou l'événement déclencheur</li>
 * <li><b>THEN (Alors):</b> Décrit le résultat attendu ou l'assertion</li>
 * </ul>
 * 
 * <p>
 * <b>Exemple:</b><br>
 * <i>GIVEN</i> l'utilisateur est connecté et sur la page de profil<br>
 * <i>WHEN</i> l'utilisateur clique sur "Modifier le profil"<br>
 * <i>THEN</i> un formulaire d'édition s'affiche avec les informations actuelles
 * </p>
 * 
 * <p>
 * <b>Caractéristiques principales:</b>
 * </p>
 * <ul>
 * <li>Définit un critère testable et vérifiable</li>
 * <li>Chaque partie (given, when, then) doit contenir entre 5 et 255
 * caractères</li>
 * <li>Possède un statut "met" indiquant si le critère est satisfait</li>
 * <li>Appartient toujours à une User Story parent</li>
 * </ul>
 * 
 * <p>
 * <b>Relations:</b>
 * </p>
 * <ul>
 * <li><b>UserStory:</b> Many-to-One - Un critère appartient à une User
 * Story</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see UserStory
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptanceCriteria {

    /**
     * Identifiant unique du critère d'acceptation généré automatiquement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Partie "GIVEN" du critère d'acceptation (format BDD).
     * Décrit le contexte ou l'état initial avant l'action.
     * Doit contenir entre 5 et 255 caractères et ne peut pas être vide.
     * Exemple: "l'utilisateur est connecté et sur la page d'accueil"
     */
    @NotBlank(message = "Given condition is required")
    @Size(min = 5, max = 255, message = "Given condition must be between 5 and 255 characters")
    private String given;

    /**
     * Partie "WHEN" du critère d'acceptation (format BDD).
     * Décrit l'action ou l'événement qui se produit.
     * Doit contenir entre 5 et 255 caractères et ne peut pas être vide.
     * Exemple: "l'utilisateur clique sur le bouton Connexion"
     * Note: Mappé à la colonne "_when" car "when" est un mot-clé SQL réservé.
     */
    @Column(name = "_when")
    @NotBlank(message = "When condition is required")
    @Size(min = 5, max = 255, message = "When condition must be between 5 and 255 characters")
    private String when;

    /**
     * Partie "THEN" du critère d'acceptation (format BDD).
     * Décrit le résultat attendu ou l'assertion à vérifier.
     * Doit contenir entre 5 et 255 caractères et ne peut pas être vide.
     * Exemple: "l'utilisateur est redirigé vers son tableau de bord"
     * Note: Mappé à la colonne "_then" car "then" est un mot-clé SQL réservé.
     */
    @Column(name = "_then")
    @NotBlank(message = "Then condition is required")
    @Size(min = 5, max = 255, message = "Then condition must be between 5 and 255 characters")
    private String then;

    /**
     * Indique si ce critère d'acceptation est satisfait/rempli.
     * true = le critère est validé, false = en attente de validation.
     * Ce champ est obligatoire.
     */
    @NotNull(message = "Acceptance criteria met status is required")
    private boolean met;

    /**
     * User Story parent à laquelle appartient ce critère d'acceptation.
     * Relation obligatoire, chargée en mode lazy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;
}