package ma.ensa.apms.service.helper;

import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.repository.ProductBacklogRepository;

/**
 * Helper class for ProductBacklog repository operations.
 * Provides centralized methods for retrieving product backlogs with proper
 * error handling.
 */
@Component
@AllArgsConstructor
public class ProductBacklogRepositoryHelper {
    private final ProductBacklogRepository productBacklogRepository;

    /**
     * Find a product backlog by ID or throw ResourceNotFoundException
     *
     * @param id the product backlog ID
     * @return the found ProductBacklog entity
     * @throws ResourceNotFoundException if product backlog not found
     */
    public ProductBacklog findByIdOrThrow(UUID id) {
        return productBacklogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product backlog not found"));
    }

    /**
     * Check if a product backlog exists by ID
     *
     * @param id the product backlog ID
     * @throws ResourceNotFoundException if product backlog does not exist
     */
    public void validateExists(UUID id) {
        if (!productBacklogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product backlog not found");
        }
    }
}
