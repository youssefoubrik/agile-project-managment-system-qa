package ma.ensa.apms.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.persistence.EntityNotFoundException;
import ma.ensa.apms.dto.Response.ApiErrorResponse;
import ma.ensa.apms.exception.BusinessException;
import ma.ensa.apms.exception.DuplicateResourceException;
import ma.ensa.apms.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private WebRequest createMockWebRequest(String path) {
        WebRequest webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=" + path);
        return webRequest;
    }

    @Test
    void handleBusinessException_ShouldReturnBadRequest() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        BusinessException exception = new BusinessException("Business rule violation");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleBusinessException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Business rule violation", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
        assertEquals("Bad Request", response.getBody().getError());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void handleMethodArgumentNotValidException_ShouldReturnBadRequestWithValidationErrors() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError fieldError1 = new FieldError("taskRequestDto", "title", "Title is required");
        FieldError fieldError2 = new FieldError("taskRequestDto", "description", "Description is required");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleMethodArgumentNotValidException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getBody().getStatus());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());

        Map<String, String> validationErrors = response.getBody().getValidationErrors();
        assertNotNull(validationErrors);
        assertEquals(2, validationErrors.size());
        assertEquals("Title is required", validationErrors.get("title"));
        assertEquals("Description is required", validationErrors.get("description"));
    }

    @Test
    void handleResourceNotFoundException_ShouldReturnNotFound() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleResourceNotFound(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
        assertEquals("Not Found", response.getBody().getError());
    }

    @Test
    void handleDuplicateResourceException_ShouldReturnConflict() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        DuplicateResourceException exception = new DuplicateResourceException("Duplicate resource");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleDuplicateResource(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CONFLICT.value(), response.getBody().getStatus());
        assertEquals("Duplicate resource", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
        assertEquals("Conflict", response.getBody().getError());
    }

    @Test
    void handleNoResourceFoundException_ShouldReturnNotFound() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        NoResourceFoundException exception = mock(NoResourceFoundException.class);

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleNoResourceFoundException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleHttpRequestMethodNotSupportedException_ShouldReturnMethodNotAllowed() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleHttpRequestMethodNotSupported(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.value(), response.getBody().getStatus());
        assertEquals("Method not allowed", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
        assertEquals("Method Not Allowed", response.getBody().getError());
    }

    @Test
    void handleEntityNotFoundException_ShouldReturnNotFound() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleEntityNotFoundException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
        assertEquals("Entity not found", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
    }

    @Test
    void handleMethodArgumentTypeMismatchException_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getName()).thenReturn("id");

        // Act
        ResponseEntity<String> response = globalExceptionHandler.handleTypeMismatch(exception);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid value for parameter: id", response.getBody());
    }

    @Test
    void handleGlobalException_ShouldReturnInternalServerError() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        Exception exception = new Exception("Unexpected error");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleGlobalException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
        assertEquals("/api/test", response.getBody().getPath());
        assertEquals("Internal Server Error", response.getBody().getError());
    }

    @Test
    void handleBusinessException_WithEmptyMessage_ShouldReturnBadRequest() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/test");
        BusinessException exception = new BusinessException("");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleBusinessException(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("", response.getBody().getMessage());
    }

    @Test
    void handleResourceNotFoundException_WithComplexPath_ShouldReturnNotFoundWithCorrectPath() {
        // Arrange
        WebRequest webRequest = createMockWebRequest("/api/tasks/123/subtasks/456");
        ResourceNotFoundException exception = new ResourceNotFoundException("Subtask not found");

        // Act
        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler
                .handleResourceNotFound(exception, webRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("/api/tasks/123/subtasks/456", response.getBody().getPath());
    }
}
