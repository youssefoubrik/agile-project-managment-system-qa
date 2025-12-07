package ma.ensa.apms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.ensa.apms.modal.Project;
import ma.ensa.apms.modal.enums.ProjectStatus;

/**
 * Repository Spring Data JPA pour l'entité Project.
 * 
 * <p>
 * Fournit les opérations CRUD de base via JpaRepository et des méthodes
 * de recherche personnalisées pour les projets.
 * </p>
 * 
 * <p>
 * <b>Méthodes héritées de JpaRepository:</b>
 * </p>
 * <ul>
 * <li>save() - Persiste ou met à jour un projet</li>
 * <li>findById() - Recherche par identifiant</li>
 * <li>findAll() - Récupère tous les projets</li>
 * <li>deleteById() - Supprime un projet par ID</li>
 * <li>count() - Compte le nombre de projets</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see Project
 * @see ProjectStatus
 */
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    /**
     * Recherche les projets dont les dates se situent dans une plage donnée.
     * 
     * @param startDate date de début minimale
     * @param endDate   date de fin maximale
     * @return liste des projets correspondants
     */
    List<Project> findByStartDateAfterAndEndDateBefore(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Recherche les projets par statut.
     * 
     * @param status le statut recherché
     * @return liste des projets ayant ce statut
     */
    List<Project> findByStatus(ProjectStatus status);
}
