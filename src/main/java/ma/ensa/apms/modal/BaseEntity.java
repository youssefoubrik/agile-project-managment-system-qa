package ma.ensa.apms.modal;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Entité de base abstraite qui fournit des propriétés communes d'audit pour
 * toutes les entités du système.
 * 
 * <p>
 * Cette classe fournit des fonctionnalités automatiques de suivi des dates de
 * création et de modification
 * pour toutes les entités qui en héritent. Les dates sont automatiquement
 * définies lors de la persistance
 * et de la mise à jour via les callbacks JPA {@link PrePersist} et
 * {@link PreUpdate}.
 * </p>
 * 
 * <p>
 * <b>Propriétés d'audit:</b>
 * </p>
 * <ul>
 * <li><b>createdAt:</b> Date et heure de création de l'entité (non modifiable
 * après création)</li>
 * <li><b>updatedAt:</b> Date et heure de la dernière modification de
 * l'entité</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 */
@MappedSuperclass
@Getter
public class BaseEntity {
    /**
     * Date et heure de création de l'entité.
     * Cette valeur est automatiquement définie lors de la première persistance et
     * ne peut pas être modifiée.
     */
    @NotNull(message = "Created At cannot be null")
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /**
     * Date et heure de la dernière modification de l'entité.
     * Cette valeur est automatiquement mise à jour à chaque modification.
     */
    @NotNull(message = "Updated At cannot be null")
    private LocalDateTime updatedAt;

    /**
     * Callback JPA exécuté avant la première persistance de l'entité.
     * Initialise les champs {@code createdAt} et {@code updatedAt} à la date et
     * heure actuelles.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Callback JPA exécuté avant chaque mise à jour de l'entité.
     * Met à jour le champ {@code updatedAt} avec la date et heure actuelles.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
