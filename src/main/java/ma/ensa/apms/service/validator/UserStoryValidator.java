package ma.ensa.apms.service.validator;

import org.springframework.stereotype.Component;

import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryStatus;

/**
 * Validator class for User Story business rules
 * Reduces complexity by centralizing validation logic
 */
@Component
public class UserStoryValidator {

    /**
     * Validate that all acceptance criteria are met before marking as DONE
     * 
     * @param story the user story to validate
     * @throws BusinessException if not all acceptance criteria are met
     */
    public void validateCanMarkAsDone(UserStory story) {
        story.getAcceptanceCriterias().stream()
                .filter(criteria -> !criteria.isMet())
                .findFirst()
                .ifPresent(criteria -> {
                    throw new BusinessException("All acceptance criteria must be met to mark as DONE.");
                });
    }

    /**
     * Validate that the user story is in TO_DO status
     * 
     * @param story the user story to validate
     * @throws BusinessException if the user story is not in TO_DO status
     */
    public void validateIsTodoStatus(UserStory story) {
        if (story.getStatus() != UserStoryStatus.TODO) {
            throw new BusinessException("Only stories in TODO status can be modified.");
        }
    }

    /**
     * Validate that the user story can be deleted
     * 
     * @param story the user story to validate
     * @throws BusinessException if the user story is not in TO_DO status
     */
    public void validateCanDelete(UserStory story) {
        if (story.getStatus() != UserStoryStatus.TODO) {
            throw new BusinessException("Only stories in TODO state can be deleted.");
        }
    }

    /**
     * Validate that the user story can be linked to an epic
     * 
     * @param story the user story to validate
     * @throws BusinessException if the user story is not in TO_DO status
     */
    public void validateCanLinkToEpic(UserStory story) {
        if (story.getStatus() != UserStoryStatus.TODO) {
            throw new BusinessException("Cannot link an epic to a user story with status higher than TODO");
        }
    }
}
