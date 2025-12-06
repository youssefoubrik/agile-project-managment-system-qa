package ma.ensa.apms.service.validator;

import org.springframework.stereotype.Component;

import ma.ensa.apms.exception.ResourceNotFoundException;
import ma.ensa.apms.modal.ProductBacklog;
import ma.ensa.apms.modal.Project;

/**
 * Validator class for ProductBacklog business rules.
 * Handles validation logic for product backlog constraints.
 */
@Component
public class ProductBacklogValidator {

    /**
     * Validate that product backlog has an associated project
     *
     * @param productBacklog the product backlog to validate
     * @throws ResourceNotFoundException if no project is associated
     */
    public void validateHasProject(ProductBacklog productBacklog) {
        Project project = productBacklog.getProject();
        if (project == null) {
            throw new ResourceNotFoundException("No project associated with this product backlog");
        }
    }
}
