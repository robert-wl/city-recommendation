package com.robert.codingchallenge.config;

import com.robert.codingchallenge.model.dto.response.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Hidden
@ControllerAdvice
public class GlobalExceptionAdvice {
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex) {
		ErrorResponseDTO error = new ErrorResponseDTO(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				Map.of("error", ex.getMessage())
		);
		return ResponseEntity.badRequest().body(error);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult()
				.getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		ErrorResponseDTO error = new ErrorResponseDTO(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				errors
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponseDTO> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {

		String parameterName = ex.getName();
		String invalidValue = ex.getValue().toString();
		String expectedType = ex.getRequiredType().getSimpleName();

		String errorMessage = String.format("Invalid value '%s' for parameter '%s'. Expected type: %s.", invalidValue, parameterName, expectedType);

		ErrorResponseDTO error = new ErrorResponseDTO(
				HttpStatus.BAD_REQUEST.value(),
				HttpStatus.BAD_REQUEST.getReasonPhrase(),
				Map.of(parameterName, errorMessage)
		);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
		log.error("An unexpected error occurred.", ex);
		ErrorResponseDTO error = new ErrorResponseDTO(
				HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
				Map.of("error", "An unexpected error occurred. Please try again later.")
		);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}
