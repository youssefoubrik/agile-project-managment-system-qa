package ma.ensa.apms.service.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.modal.AcceptanceCriteria;
import ma.ensa.apms.modal.UserStory;
import ma.ensa.apms.modal.enums.UserStoryStatus;

class UserStoryValidatorTest {

    private UserStoryValidator validator;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        validator = new UserStoryValidator();
        userStory = new UserStory();
    }

    @Test
    void validateCanMarkAsDone_WhenAllCriteriaMet_ShouldNotThrow() {
        // Arrange
        List<AcceptanceCriteria> criterias = new ArrayList<>();
        AcceptanceCriteria criteria1 = new AcceptanceCriteria();
        criteria1.setMet(true);
        AcceptanceCriteria criteria2 = new AcceptanceCriteria();
        criteria2.setMet(true);
        criterias.add(criteria1);
        criterias.add(criteria2);
        userStory.setAcceptanceCriterias(criterias);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateCanMarkAsDone(userStory));
    }

    @Test
    void validateCanMarkAsDone_WhenNoCriteria_ShouldNotThrow() {
        // Arrange
        userStory.setAcceptanceCriterias(new ArrayList<>());

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateCanMarkAsDone(userStory));
    }

    @Test
    void validateCanMarkAsDone_WhenSomeCriteriaNotMet_ShouldThrowException() {
        // Arrange
        List<AcceptanceCriteria> criterias = new ArrayList<>();
        AcceptanceCriteria criteria1 = new AcceptanceCriteria();
        criteria1.setMet(true);
        AcceptanceCriteria criteria2 = new AcceptanceCriteria();
        criteria2.setMet(false);
        criterias.add(criteria1);
        criterias.add(criteria2);
        userStory.setAcceptanceCriterias(criterias);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateCanMarkAsDone(userStory));
        assertEquals("All acceptance criteria must be met to mark as DONE.", exception.getMessage());
    }

    @Test
    void validateIsTodoStatus_WhenTodoStatus_ShouldNotThrow() {
        // Arrange
        userStory.setStatus(UserStoryStatus.TODO);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateIsTodoStatus(userStory));
    }

    @Test
    void validateIsTodoStatus_WhenNotTodoStatus_ShouldThrowException() {
        // Arrange
        userStory.setStatus(UserStoryStatus.IN_PROGRESS);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateIsTodoStatus(userStory));
        assertEquals("Only stories in TODO status can be modified.", exception.getMessage());
    }

    @Test
    void validateCanDelete_WhenTodoStatus_ShouldNotThrow() {
        // Arrange
        userStory.setStatus(UserStoryStatus.TODO);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateCanDelete(userStory));
    }

    @Test
    void validateCanDelete_WhenInProgressStatus_ShouldThrowException() {
        // Arrange
        userStory.setStatus(UserStoryStatus.IN_PROGRESS);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateCanDelete(userStory));
        assertEquals("Only stories in TODO state can be deleted.", exception.getMessage());
    }

    @Test
    void validateCanDelete_WhenDoneStatus_ShouldThrowException() {
        // Arrange
        userStory.setStatus(UserStoryStatus.DONE);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateCanDelete(userStory));
        assertEquals("Only stories in TODO state can be deleted.", exception.getMessage());
    }

    @Test
    void validateCanLinkToEpic_WhenTodoStatus_ShouldNotThrow() {
        // Arrange
        userStory.setStatus(UserStoryStatus.TODO);

        // Act & Assert
        assertDoesNotThrow(() -> validator.validateCanLinkToEpic(userStory));
    }

    @Test
    void validateCanLinkToEpic_WhenInProgressStatus_ShouldThrowException() {
        // Arrange
        userStory.setStatus(UserStoryStatus.IN_PROGRESS);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateCanLinkToEpic(userStory));
        assertEquals("Cannot link an epic to a user story with status higher than TODO", exception.getMessage());
    }

    @Test
    void validateCanLinkToEpic_WhenDoneStatus_ShouldThrowException() {
        // Arrange
        userStory.setStatus(UserStoryStatus.DONE);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> validator.validateCanLinkToEpic(userStory));
        assertEquals("Cannot link an epic to a user story with status higher than TODO", exception.getMessage());
    }
}
