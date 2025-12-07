package ma.ensa.apms.service;

import java.util.List;
import java.util.UUID;

import ma.ensa.apms.dto.Request.UserStoryRequest;
import ma.ensa.apms.dto.Response.AcceptanceCriteriaResponse;
import ma.ensa.apms.dto.Response.UserStoryResponse;
import ma.ensa.apms.modal.enums.UserStoryStatus;

/**
 * Interface de service pour la gestion des User Stories.
 * 
 * <p>
 * Ce service fournit toutes les opérations métier relatives aux User Stories
 * dans le système
 * de gestion de projet Agile, incluant la création, modification, consultation,
 * changement de statut,
 * liaison avec des Epics et assignation à des Sprints.
 * </p>
 * 
 * <p>
 * <b>Fonctionnalités principales:</b>
 * </p>
 * <ul>
 * <li>CRUD complet des User Stories</li>
 * <li>Gestion du cycle de vie (changement de statut)</li>
 * <li>Organisation hiérarchique (liaison avec Epic)</li>
 * <li>Planification de sprint (assignation à Sprint Backlog)</li>
 * <li>Consultation des critères d'acceptation</li>
 * <li>Recherche par différents critères (statut, Epic, Sprint, Product
 * Backlog)</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ma.ensa.apms.service.impl.UserStoryServiceImpl
 * @see UserStoryRequest
 * @see UserStoryResponse
 */
public interface UserStoryService {

    /**
     * Crée une nouvelle User Story.
     * 
     * @param dto les données de la User Story à créer
     * @return la User Story créée avec son identifiant généré
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si le Product
     *                                                          Backlog référencé
     *                                                          n'existe pas
     */
    UserStoryResponse create(UserStoryRequest dto);

    /**
     * Met à jour une User Story existante.
     * 
     * @param id  l'identifiant de la User Story à modifier
     * @param dto les nouvelles données de la User Story
     * @return la User Story mise à jour
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story
     *                                                          n'est pas trouvée
     */
    UserStoryResponse updateUserStory(UUID id, UserStoryRequest dto);

    /**
     * Récupère une User Story par son identifiant.
     * 
     * @param id l'identifiant de la User Story
     * @return la User Story demandée
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story
     *                                                          n'est pas trouvée
     */
    UserStoryResponse getUserStoryById(UUID id);

    /**
     * Change le statut d'une User Story.
     * 
     * <p>
     * Si le nouveau statut est DONE, vérifie que tous les critères d'acceptation
     * sont satisfaits.
     * </p>
     * 
     * @param id        l'identifiant de la User Story
     * @param newStatus le nouveau statut à appliquer
     * @return la User Story avec le statut mis à jour
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story
     *                                                          n'est pas trouvée
     * @throws ma.ensa.apms.exception.BusinessException         si le changement de
     *                                                          statut n'est pas
     *                                                          autorisé
     */
    UserStoryResponse changeStatus(UUID id, UserStoryStatus newStatus);

    /**
     * Lie une User Story à un Epic.
     * 
     * <p>
     * Permet de regrouper thématiquement les User Stories. La User Story doit être
     * en statut TO DO.
     * </p>
     * 
     * @param storyId l'identifiant de la User Story à lier
     * @param epicId  l'identifiant de l'Epic
     * @return la User Story liée à l'Epic
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story ou
     *                                                          l'Epic n'est pas
     *                                                          trouvé
     * @throws ma.ensa.apms.exception.BusinessException         si la User Story
     *                                                          n'est pas en statut
     *                                                          TO DO
     */
    UserStoryResponse linkToEpic(UUID storyId, UUID epicId);

    /**
     * Déplace une User Story vers un Sprint Backlog pour planification.
     * 
     * <p>
     * La User Story est retirée du Product Backlog et ajoutée au Sprint Backlog
     * spécifié.
     * </p>
     * 
     * @param storyId  l'identifiant de la User Story à déplacer
     * @param sprintId l'identifiant du Sprint Backlog de destination
     * @return la User Story déplacée dans le Sprint
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story ou
     *                                                          le Sprint n'est pas
     *                                                          trouvé
     */
    UserStoryResponse moveToSprint(UUID storyId, UUID sprintId);

    /**
     * Récupère tous les critères d'acceptation d'une User Story.
     * 
     * @param id l'identifiant de la User Story
     * @return la liste des critères d'acceptation
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story
     *                                                          n'est pas trouvée
     */
    List<AcceptanceCriteriaResponse> getAcceptanceCriteriasByUserStoryId(UUID id);

    /**
     * Recherche les User Stories par statut dans un Product Backlog.
     * 
     * @param statut           le statut recherché
     * @param productBacklogId l'identifiant du Product Backlog
     * @return la liste des User Stories correspondantes
     */
    List<UserStoryResponse> getUserStoriesByStatusAndProductBacklogId(UserStoryStatus statut, UUID productBacklogId);

    /**
     * Récupère toutes les User Stories d'un Epic.
     * 
     * @param epicId l'identifiant de l'Epic
     * @return la liste des User Stories de l'Epic
     */
    List<UserStoryResponse> getUserStoriesByEpicId(UUID epicId);

    /**
     * Récupère toutes les User Stories d'un Sprint Backlog.
     * 
     * @param sprintId l'identifiant du Sprint Backlog
     * @return la liste des User Stories du Sprint
     */
    List<UserStoryResponse> getUserStoriesBySprintBacklogId(UUID sprintId);

    /**
     * Supprime une User Story.
     * 
     * <p>
     * Cette opération supprime également en cascade tous les critères d'acceptation
     * et tâches associés à la User Story.
     * </p>
     * 
     * @param id l'identifiant de la User Story à supprimer
     * @throws ma.ensa.apms.exception.ResourceNotFoundException si la User Story
     *                                                          n'est pas trouvée
     */
    void delete(UUID id);
}
