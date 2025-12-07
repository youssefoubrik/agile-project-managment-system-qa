package ma.ensa.apms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lorsqu'une tentative de création d'une ressource dupliquée
 * est détectée.
 * 
 * <p>
 * Cette exception est utilisée pour signaler qu'une tentative d'insertion ou de
 * mise à jour
 * viole une contrainte d'unicité dans la base de données.
 * </p>
 * 
 * <p>
 * <b>Exemples d'utilisation:</b>
 * </p>
 * <ul>
 * <li>Création d'un projet avec un nom déjà existant</li>
 * <li>Tentative d'ajout d'une User Story déjà liée à un Epic</li>
 * <li>Violation de contraintes UNIQUE sur des colonnes</li>
 * <li>Duplication de clés métier</li>
 * </ul>
 * 
 * <p>
 * Cette exception est automatiquement mappée au code HTTP 409 (Conflict)
 * grâce à l'annotation @ResponseStatus.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ma.ensa.apms.advice.GlobalExceptionHandler
 * @see BusinessException
 * @see ResourceNotFoundException
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateResourceException extends RuntimeException {

    /**
     * Construit une nouvelle exception de ressource dupliquée.
     * 
     * @param message le message de détail expliquant la nature de la duplication
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}