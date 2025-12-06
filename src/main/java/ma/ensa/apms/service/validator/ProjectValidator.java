package ma.ensa.apms.service.validator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.modal.Project;

/**
 * Validator class for Project business rules.
 * Handles validation logic for project dates and other constraints.
 */
@Component
public class ProjectValidator {

    /**
     * Validate that start date is before end date
     *
     * @param startDate the start date to validate
     * @param endDate   the end date to compare with
     * @throws BusinessException if start date is not before end date
     */
    public void validateStartDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate != null && endDate.isBefore(startDate)) {
            throw new BusinessException("Start date must be before end date");
        }
    }

    /**
     * Validate that end date is after start date
     *
     * @param startDate the start date to compare with
     * @param endDate   the end date to validate
     * @throws BusinessException if end date is not after start date
     */
    public void validateEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && startDate.isAfter(endDate)) {
            throw new BusinessException("End date must be after start date");
        }
    }

    /**
     * Validate that project doesn't already have a product backlog assigned
     *
     * @param project the project to validate
     * @throws IllegalStateException if project already has a product backlog
     */
    public void validateProductBacklogAssignment(Project project) {
        if (project.getProductBacklog() != null) {
            throw new IllegalStateException("This project already has a ProductBacklog assigned");
        }
    }
}
