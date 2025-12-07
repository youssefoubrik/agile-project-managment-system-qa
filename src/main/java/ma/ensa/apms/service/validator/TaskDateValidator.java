package ma.ensa.apms.service.validator;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import ma.ensa.apms.exception.BusinessException;

/**
 * Validator class for Task date validations.
 * 
 * <p>
 * Encapsulates all date-related validation logic for Tasks
 * to reduce complexity and ensure consistent validation rules.
 * </p>
 * 
 * <p>
 * <b>Responsibilities:</b>
 * </p>
 * <ul>
 * <li>Validate start date against end date</li>
 * <li>Validate end date against start date</li>
 * <li>Centralized date validation error messages</li>
 * </ul>
 * 
 * @author APMS Team
 * @version 1.0
 * @since 1.0
 */
@Component
public class TaskDateValidator {

    /**
     * Validates that a new start date is not after the existing end date.
     * 
     * @param newStartDate    the new start date to validate
     * @param existingEndDate the existing end date (can be null)
     * @throws BusinessException if start date is after end date
     */
    public void validateStartDate(LocalDateTime newStartDate, LocalDateTime existingEndDate) {
        if (existingEndDate != null && newStartDate.isAfter(existingEndDate)) {
            throw new BusinessException("Start date cannot be after the end date");
        }
    }

    /**
     * Validates that a new end date is not before the existing start date.
     * 
     * @param existingStartDate the existing start date (can be null)
     * @param newEndDate        the new end date to validate
     * @throws BusinessException if end date is before start date
     */
    public void validateEndDate(LocalDateTime existingStartDate, LocalDateTime newEndDate) {
        if (existingStartDate != null && newEndDate.isBefore(existingStartDate)) {
            throw new BusinessException("End date cannot be before the start date");
        }
    }
}
