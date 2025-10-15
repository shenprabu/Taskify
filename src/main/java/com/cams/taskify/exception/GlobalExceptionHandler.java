package com.cams.taskify.exception;

import com.cams.taskify.constants.TaskStatus;
import com.cams.taskify.response.ErrorResponse;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String field = error instanceof FieldError ? ((FieldError) error).getField() : "";
                    return field.isEmpty() ? error.getDefaultMessage() : field + ": " + error.getDefaultMessage();
                })
                .collect(Collectors.toList());

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Invalid input parameters",
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle input type mismatch errors
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {
        String details = null;
        if(ex.getMessage().contains("Method parameter 'status'")) {
            String validStatuses = Arrays.stream(TaskStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.joining(", "));
            details = "Status value should be in [" + validStatuses + "]";
        }
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                "Invalid input parameters",
                List.of(details != null ? details : ex.getMessage())
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // DB integrity exception handler
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {

        List<String> errors;

        String message = "Data integrity violation";

        // Optional: check if it’s the email unique constraint
        if (ex.getMostSpecificCause().getMessage().contains("email")) {
            message = "Email must be unique";
            errors = List.of("email: Email already exists");
        } else {
            errors = List.of(ex.getMostSpecificCause().getMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                message,
                errors
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handles unknown/malformed input field errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        ErrorResponse error;
        if (cause instanceof UnrecognizedPropertyException unrecognized) {
            String message = "Unknown field: " + unrecognized.getPropertyName();
            error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "INVALID_FIELD",
                    message,
                    List.of(message)
            );
        } else {
            error = new ErrorResponse(
                    HttpStatus.BAD_REQUEST.value(),
                    "MALFORMED_JSON",
                    "Invalid JSON input",
                    List.of(ex.getMessage())
            );
        }
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handles resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Resource Not Found",
                ex.getMessage(),
                List.of(ex.getMessage())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Server Error",
                ex.getMessage(),
                List.of("Unexpected error occurred")
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
