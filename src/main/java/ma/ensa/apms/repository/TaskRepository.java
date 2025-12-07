package ma.ensa.apms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.Task;
import ma.ensa.apms.modal.enums.TaskStatus;

/**
 * Repository Spring Data JPA pour l'entité Task.
 * 
 * <p>
 * Fournit les opérations CRUD de base et des méthodes de recherche
 * pour les tâches, incluant les recherches par statut et par plage de dates.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see Task
 * @see TaskStatus
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    
    /**
     * Recherche les tâches par statut.
     * 
     * @param status le statut recherché
     * @return liste des tâches ayant ce statut
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Recherche les tâches dont les dates se situent dans une plage donnée.
     * 
     * @param startDate date de début minimale
     * @param endDate date de fin maximale
     * @return liste des tâches correspondantes
     */
    List<Task> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDateTime startDate, LocalDateTime endDate);
}
