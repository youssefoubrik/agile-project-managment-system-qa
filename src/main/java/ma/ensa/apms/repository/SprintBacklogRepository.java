package ma.ensa.apms.repository;

import ma.ensa.apms.modal.SprintBacklog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repository Spring Data JPA pour l'entité SprintBacklog.
 * 
 * <p>
 * Fournit les opérations CRUD de base pour les Sprint Backlogs
 * (sélection de User Stories à réaliser durant un sprint).
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see SprintBacklog
 */
public interface SprintBacklogRepository extends JpaRepository<SprintBacklog, UUID> {
}