package ma.ensa.apms.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link LoggerUtils}
 */
@DisplayName("LoggerUtils Tests")
class LoggerUtilsTest {

    private ListAppender<ILoggingEvent> serviceLogAppender;
    private ListAppender<ILoggingEvent> authLogAppender;
    private Logger serviceLogger;
    private Logger authLogger;

    @BeforeEach
    void setUp() {
        // Get the service logger and configure appender
        serviceLogger = (Logger) LoggerFactory.getLogger("ma.ensa.apms.service.impl");
        serviceLogAppender = new ListAppender<>();
        serviceLogAppender.start();
        serviceLogger.addAppender(serviceLogAppender);

        // Get the auth logger and configure appender
        authLogger = (Logger) LoggerFactory.getLogger("ma.ensa.apms.auth");
        authLogAppender = new ListAppender<>();
        authLogAppender.start();
        authLogger.addAppender(authLogAppender);
    }

    @AfterEach
    void tearDown() {
        serviceLogger.detachAppender(serviceLogAppender);
        authLogger.detachAppender(authLogAppender);
    }

    @Test
    @DisplayName("logServiceInfo should log info message")
    void logServiceInfo_ShouldLogInfoMessage() {
        // Given
        String message = "Test service info message";

        // When
        LoggerUtils.logServiceInfo(message);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("logServiceInfo with format should log formatted info message")
    void logServiceInfo_WithFormat_ShouldLogFormattedInfoMessage() {
        // Given
        String format = "Service {} executed with status {}";
        Object arg1 = "createProject";
        Object arg2 = "SUCCESS";

        // When
        LoggerUtils.logServiceInfo(format, arg1, arg2);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("Service createProject executed with status SUCCESS");
    }

    @Test
    @DisplayName("logServiceError with exception should log error message with throwable")
    void logServiceError_WithException_ShouldLogErrorMessageWithThrowable() {
        // Given
        String message = "Test service error message";
        Exception exception = new RuntimeException("Test exception");

        // When
        LoggerUtils.logServiceError(message, exception);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.ERROR);
        assertThat(logEvent.getFormattedMessage()).isEqualTo(message);
        assertThat(logEvent.getThrowableProxy()).isNotNull();
        assertThat(logEvent.getThrowableProxy().getMessage()).isEqualTo("Test exception");
    }

    @Test
    @DisplayName("logServiceError with format should log formatted error message")
    void logServiceError_WithFormat_ShouldLogFormattedErrorMessage() {
        // Given
        String format = "Error in service {} with code {}";
        Object arg1 = "updateProject";
        Object arg2 = 500;

        // When
        LoggerUtils.logServiceError(format, arg1, arg2);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.ERROR);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("Error in service updateProject with code 500");
    }

    @Test
    @DisplayName("logServiceDebug should log debug message")
    void logServiceDebug_ShouldLogDebugMessage() {
        // Given
        String message = "Test service debug message";
        serviceLogger.setLevel(Level.DEBUG);

        // When
        LoggerUtils.logServiceDebug(message);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.DEBUG);
        assertThat(logEvent.getFormattedMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("logServiceDebug with format should log formatted debug message")
    void logServiceDebug_WithFormat_ShouldLogFormattedDebugMessage() {
        // Given
        String format = "Debug: Method {} called with param {}";
        Object arg1 = "deleteProject";
        Object arg2 = 123;
        serviceLogger.setLevel(Level.DEBUG);

        // When
        LoggerUtils.logServiceDebug(format, arg1, arg2);

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.DEBUG);
        assertThat(logEvent.getFormattedMessage()).isEqualTo("Debug: Method deleteProject called with param 123");
    }

    @Test
    @DisplayName("logAuthInfo should log authentication info message")
    void logAuthInfo_ShouldLogAuthenticationInfoMessage() {
        // Given
        String message = "User authenticated successfully";

        // When
        LoggerUtils.logAuthInfo(message);

        // Then
        List<ILoggingEvent> logsList = authLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.INFO);
        assertThat(logEvent.getFormattedMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("logAuthError should log authentication error message with throwable")
    void logAuthError_ShouldLogAuthenticationErrorMessageWithThrowable() {
        // Given
        String message = "Authentication failed";
        Exception exception = new SecurityException("Invalid credentials");

        // When
        LoggerUtils.logAuthError(message, exception);

        // Then
        List<ILoggingEvent> logsList = authLogAppender.list;
        assertThat(logsList).hasSize(1);
        ILoggingEvent logEvent = logsList.get(0);
        assertThat(logEvent.getLevel()).isEqualTo(Level.ERROR);
        assertThat(logEvent.getFormattedMessage()).isEqualTo(message);
        assertThat(logEvent.getThrowableProxy()).isNotNull();
        assertThat(logEvent.getThrowableProxy().getMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    @DisplayName("logServiceInfo with null message should not throw exception")
    void logServiceInfo_WithNullMessage_ShouldNotThrowException() {
        // Given
        String message = null;

        // When & Then - should not throw exception
        LoggerUtils.logServiceInfo(message);

        // Verify log was created
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getFormattedMessage()).isNull();
    }

    @Test
    @DisplayName("logServiceError with null message and null exception should not throw exception")
    void logServiceError_WithNullMessageAndNullException_ShouldNotThrowException() {
        // Given
        String message = null;
        Throwable exception = null;

        // When & Then - should not throw exception
        LoggerUtils.logServiceError(message, exception);

        // Verify log was created
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(1);
        assertThat(logsList.get(0).getFormattedMessage()).isNull();
    }

    @Test
    @DisplayName("multiple log calls should accumulate in appender")
    void multipleLogCalls_ShouldAccumulateInAppender() {
        // When
        LoggerUtils.logServiceInfo("Message 1");
        LoggerUtils.logServiceInfo("Message 2");
        LoggerUtils.logServiceDebug("Message 3");

        // Then
        List<ILoggingEvent> logsList = serviceLogAppender.list;
        assertThat(logsList).hasSize(3);
        assertThat(logsList.get(0).getFormattedMessage()).isEqualTo("Message 1");
        assertThat(logsList.get(1).getFormattedMessage()).isEqualTo("Message 2");
        assertThat(logsList.get(2).getFormattedMessage()).isEqualTo("Message 3");
    }

    @Test
    @DisplayName("service and auth logs should be independent")
    void serviceAndAuthLogs_ShouldBeIndependent() {
        // When
        LoggerUtils.logServiceInfo("Service message");
        LoggerUtils.logAuthInfo("Auth message");

        // Then
        List<ILoggingEvent> serviceLogsList = serviceLogAppender.list;
        List<ILoggingEvent> authLogsList = authLogAppender.list;

        assertThat(serviceLogsList).hasSize(1);
        assertThat(serviceLogsList.get(0).getFormattedMessage()).isEqualTo("Service message");

        assertThat(authLogsList).hasSize(1);
        assertThat(authLogsList.get(0).getFormattedMessage()).isEqualTo("Auth message");
    }
}
