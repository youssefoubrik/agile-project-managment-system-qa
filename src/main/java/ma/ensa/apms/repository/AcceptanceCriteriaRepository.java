package ma.ensa.apms.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.AcceptanceCriteria;

/**
 * Repository Spring Data JPA pour l'entité AcceptanceCriteria.
 * 
 * <p>
 * Fournit les opérations CRUD de base et des méthodes de recherche
 * pour les critères d'acceptation des User Stories.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see AcceptanceCriteria
 */
@Repository
public interface AcceptanceCriteriaRepository extends JpaRepository<AcceptanceCriteria, UUID> {
    
    /**
     * Recherche les critères d'acceptation selon leur état (satisfait ou non).
     * 
     * @param met true pour les critères satisfaits, false pour les non satisfaits
     * @return liste des critères d'acceptation correspondants
     */
    List<AcceptanceCriteria> findByMet(boolean met);
}