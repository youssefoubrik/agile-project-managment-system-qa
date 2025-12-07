package ma.ensa.apms.exception;

/**
 * Exception levée lorsqu'une ressource demandée n'est pas trouvée.
 * 
 * <p>
 * Cette exception est utilisée pour signaler qu'une entité recherchée
 * par son identifiant ou d'autres critères n'existe pas dans la base de
 * données.
 * </p>
 * 
 * <p>
 * <b>Exemples d'utilisation:</b>
 * </p>
 * <ul>
 * <li>Recherche d'un projet par ID inexistant</li>
 * <li>Recherche d'une User Story qui n'existe pas</li>
 * <li>Tentative d'accès à une ressource supprimée</li>
 * <li>Référence à une entité liée qui n'existe pas</li>
 * </ul>
 * 
 * <p>
 * Cette exception est automatiquement mappée au code HTTP 404 (Not Found)
 * par le {@link ma.ensa.apms.advice.GlobalExceptionHandler}.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ma.ensa.apms.advice.GlobalExceptionHandler
 * @see BusinessException
 * @see DuplicateResourceException
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Construit une nouvelle exception de ressource non trouvée.
     * 
     * @param message le message de détail indiquant quelle ressource n'a pas été
     *                trouvée
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}