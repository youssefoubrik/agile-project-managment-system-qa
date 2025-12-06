package ma.ensa.apms.validation;

public final class ValidationConstants {

    private ValidationConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // UserStory validation constants
    public static final class UserStory {
        public static final int NAME_MIN_LENGTH = 10;
        public static final int NAME_MAX_LENGTH = 100;
        public static final String NAME_BLANK_MESSAGE = "Title cannot be blank";
        public static final String NAME_SIZE_MESSAGE = "Title must be between 10 and 100 characters";
        public static final String ROLE_BLANK_MESSAGE = "Role cannot be blank";
        public static final String FEATURE_BLANK_MESSAGE = "Feature cannot be blank";
        public static final String BENEFIT_BLANK_MESSAGE = "Benefit cannot be blank";
        public static final String PRIORITY_REQUIRED_MESSAGE = "Priority is required";
        public static final String STATUS_REQUIRED_MESSAGE = "Status is required";

        private UserStory() {
        }
    }

    // Task validation constants
    public static final class Task {
        public static final int TITLE_MIN_LENGTH = 5;
        public static final int TITLE_MAX_LENGTH = 100;
        public static final String TITLE_BLANK_MESSAGE = "Title cannot be blank";
        public static final String TITLE_SIZE_MESSAGE = "Title must be between 5 and 100 characters";
        public static final String DESCRIPTION_BLANK_MESSAGE = "Description cannot be blank";
        public static final String START_DATE_REQUIRED_MESSAGE = "Start date is required";
        public static final String END_DATE_REQUIRED_MESSAGE = "End date is required";
        public static final String STATUS_REQUIRED_MESSAGE = "Status is required";

        private Task() {
        }
    }

    // Epic validation constants
    public static final class Epic {
        public static final int NAME_MIN_LENGTH = 5;
        public static final int NAME_MAX_LENGTH = 100;
        public static final String NAME_BLANK_MESSAGE = "Name cannot be blank";
        public static final String NAME_SIZE_MESSAGE = "Name must be between 5 and 100 characters";
        public static final String DESCRIPTION_BLANK_MESSAGE = "Description cannot be blank";

        private Epic() {
        }
    }

    // Project validation constants
    public static final class Project {
        public static final int NAME_MIN_LENGTH = 3;
        public static final int NAME_MAX_LENGTH = 100;
        public static final String NAME_BLANK_MESSAGE = "Name cannot be blank";
        public static final String NAME_SIZE_MESSAGE = "Name must be between 3 and 100 characters";
        public static final String DESCRIPTION_BLANK_MESSAGE = "Description cannot be blank";
        public static final String START_DATE_REQUIRED_MESSAGE = "Start date is required";
        public static final String END_DATE_REQUIRED_MESSAGE = "End date is required";

        private Project() {
        }
    }

    // AcceptanceCriteria validation constants
    public static final class AcceptanceCriteria {
        public static final int CRITERIA_MIN_LENGTH = 10;
        public static final int CRITERIA_MAX_LENGTH = 500;
        public static final String CRITERIA_BLANK_MESSAGE = "Criteria cannot be blank";
        public static final String CRITERIA_SIZE_MESSAGE = "Criteria must be between 10 and 500 characters";

        private AcceptanceCriteria() {
        }
    }
}
