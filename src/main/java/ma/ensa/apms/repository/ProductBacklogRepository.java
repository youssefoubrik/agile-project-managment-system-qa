package ma.ensa.apms.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.ProductBacklog;

/**
 * Repository Spring Data JPA pour l'entité ProductBacklog.
 * 
 * <p>
 * Fournit les opérations CRUD de base pour les Product Backlogs
 * (liste priorisée de toutes les User Stories d'un projet).
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ProductBacklog
 */
@Repository
public interface ProductBacklogRepository extends JpaRepository<ProductBacklog, UUID> { 
    
}