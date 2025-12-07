package ma.ensa.apms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ma.ensa.apms.modal.UserStory;
import java.util.List;
import java.util.UUID;

import ma.ensa.apms.modal.enums.UserStoryStatus;

/**
 * Repository Spring Data JPA pour l'entité UserStory.
 * 
 * <p>
 * Fournit les opérations CRUD de base et des méthodes de recherche avancées
 * pour les User Stories, incluant les recherches par Product Backlog, Epic,
 * Sprint Backlog et statut.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see UserStory
 * @see UserStoryStatus
 */
@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, UUID> {
    
    /**
     * Vérifie l'existence d'une User Story avec un nom donné dans un Product Backlog.
     * 
     * @param name le nom de la User Story
     * @param productBacklogId l'identifiant du Product Backlog
     * @return true si une User Story existe avec ce nom dans ce backlog
     */
    boolean existsByNameAndProductBacklogId(String name, UUID productBacklogId);

    /**
     * Recherche les User Stories d'un Product Backlog triées par priorité croissante.
     * 
     * @param productBacklogId l'identifiant du Product Backlog
     * @return liste des User Stories triées par priorité
     */
    List<UserStory> findByProductBacklogIdOrderByPriorityAsc(UUID productBacklogId);

    /**
     * Recherche les User Stories par statut dans un Product Backlog donné.
     * 
     * @param status le statut recherché
     * @param productBacklogId l'identifiant du Product Backlog
     * @return liste des User Stories correspondantes
     */
    List<UserStory> findByStatusAndProductBacklogId(UserStoryStatus status, UUID productBacklogId);

    /**
     * Recherche les User Stories appartenant à un Epic.
     * 
     * @param epicId l'identifiant de l'Epic
     * @return liste des User Stories de cet Epic
     */
    List<UserStory> findByEpicId(UUID epicId);

    /**
     * Recherche les User Stories assignées à un Sprint Backlog.
     * 
     * @param sprintBacklogId l'identifiant du Sprint Backlog
     * @return liste des User Stories du sprint
     */
    List<UserStory> findBySprintBacklogId(UUID sprintBacklogId);

    /**
     * Recherche toutes les User Stories d'un Product Backlog.
     * 
     * @param productBacklogId l'identifiant du Product Backlog
     * @return liste des User Stories du backlog
     */
    List<UserStory> findByProductBacklogId(UUID productBacklogId);
}