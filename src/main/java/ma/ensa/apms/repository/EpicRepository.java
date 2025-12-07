package ma.ensa.apms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.Epic;

/**
 * Repository Spring Data JPA pour l'entité Epic.
 * 
 * <p>
 * Fournit les opérations CRUD de base et des méthodes de recherche
 * personnalisées pour les Epics (regroupements thématiques de User Stories).
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see Epic
 */
@Repository
public interface EpicRepository extends JpaRepository<Epic, UUID> {

    /**
     * Recherche les Epics dont le nom contient le mot-clé spécifié (insensible à la
     * casse).
     * 
     * @param keyword le mot-clé à rechercher dans le nom
     * @return liste des Epics correspondants
     */
    List<Epic> findByNameContainingIgnoreCase(String keyword);
}