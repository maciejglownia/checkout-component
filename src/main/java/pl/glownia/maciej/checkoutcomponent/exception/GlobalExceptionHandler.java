package pl.glownia.maciej.checkoutcomponent.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler for managing exceptions across the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles JSON parsing and deserialization issues, such as invalid JSON.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        System.out.println("Handling HttpMessageNotReadableException...");

        // Get the root cause of the exception
        Throwable rootCause = ex.getMostSpecificCause();
        if (rootCause instanceof JsonParseException) {
            System.out.println("Root cause is JsonParseException: " + rootCause.getMessage());

            // Return a detailed error message with cause and specifics
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(
                            "Error: Invalid JSON format.\n" +
                                    "Cause: JsonParseException\n" +
                                    "Message: " + rootCause.getMessage()
                    );
        }

        System.out.println("HttpMessageNotReadableException without JsonParseException.");
        // Return a generic invalid JSON message if cause is not JsonParseException
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: Invalid JSON format.");
    }

    /**
     * Handles cases where the HTTP method is not supported by the endpoint.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        System.out.println("Handling HttpRequestMethodNotSupportedException...");
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body("Error: HTTP method '" + ex.getMethod() + "' is not supported for this endpoint.");
    }

    /**
     * Handles cases of IllegalArgumentException and checks for wrapped JSON-related exceptions.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        System.out.println("Handling IllegalArgumentException: " + ex.getMessage());

        // Check if the exception wraps a JSON-related cause
        Throwable rootCause = ex.getCause();
        if (rootCause instanceof HttpMessageNotReadableException) {
            System.out.println("Delegating to HttpMessageNotReadableException handler...");
            return handleHttpMessageNotReadableException((HttpMessageNotReadableException) rootCause);
        }

        // Return generic IllegalArgumentException details
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: " + ex.getMessage());
    }

    /**
     * Handles any unhandled or unexpected exceptions as a fallback.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnhandledException(Exception ex) {
        System.out.println("Handling generic Exception...");
        ex.printStackTrace(); // Log stack trace for debugging (Remove in production)

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }

}