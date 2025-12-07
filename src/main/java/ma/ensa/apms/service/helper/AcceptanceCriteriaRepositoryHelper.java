package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.repository.AcceptanceCriteriaRepository;

/**
 * Helper class for AcceptanceCriteria repository operations.
 * 
 * <p>
 * Encapsulates common repository operations and validations for
 * AcceptanceCriteria
 * to reduce complexity in the service layer and promote code reuse.
 * </p>
 * 
 * <p>
 * <b>Responsibilities:</b>
 * </p>
 * <ul>
 * <li>AcceptanceCriteria retrieval with proper exception handling</li>
 * <li>Existence validation</li>
 * <li>Centralized error message management</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class AcceptanceCriteriaRepositoryHelper {

    private final AcceptanceCriteriaRepository acceptanceCriteriaRepository;

    /**
     * Finds an acceptance criteria by ID or throws ResourceNotFoundException.
     * 
     * @param id the acceptance criteria ID
     * @return the found acceptance criteria
     * @throws ResourceNotFoundException if acceptance criteria not found
     */
    public AcceptanceCriteria findByIdOrThrow(UUID id) {
        return acceptanceCriteriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Acceptance Criteria not found"));
    }

    /**
     * Validates that an acceptance criteria exists by ID.
     * 
     * @param id the acceptance criteria ID
     * @throws ResourceNotFoundException if acceptance criteria does not exist
     */
    public void validateExists(UUID id) {
        if (!acceptanceCriteriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Acceptance Criteria not found");
        }
    }
}
