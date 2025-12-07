package ma.ensa.apms.exception;

/**
 * Exception métier pour les violations de règles de gestion.
 * 
 * <p>
 * Cette exception est levée lorsqu'une opération viole une règle métier
 * de l'application, comme par exemple :
 * </p>
 * <ul>
 * <li>Tentative de transition d'état invalide</li>
 * <li>Violation de contraintes métier</li>
 * <li>Opération non autorisée dans le contexte actuel</li>
 * <li>Validation de règles métier complexes échouée</li>
 * </ul>
 * 
 * <p>
 * Cette exception est une RuntimeException et n'a pas besoin d'être déclarée
 * dans les signatures de méthodes.
 * </p>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 * @see ResourceNotFoundException
 * @see DuplicateResourceException
 */
public class BusinessException extends RuntimeException {

    /**
     * Construit une nouvelle exception métier avec le message spécifié.
     * 
     * @param message le message de détail expliquant la violation de règle métier
     */
    public BusinessException(String message) {
        super(message);
    }
}