package com.example.demo.exception;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestApiControllerAdvice extends ResponseEntityExceptionHandler{
	
	private final record ErrorResponse(String message, String code) {};

	
	//RequestBodyとValidatedの際の例外	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request){
		return buildErrorResponse(ex);
		
	}
	
	//RequestParamなどでbindingする際に起きうる例外
	@ExceptionHandler(BindException.class)
	public ResponseEntity<?> handleBindException(BindException ex){
		return buildErrorResponse(ex.getBindingResult());
	}	
	
	private ResponseEntity<Object> buildErrorResponse(BindingResult bindingResult){
		
		List<Map<String,String>> errors = bindingResult.getFieldErrors().stream()
				.map(error -> Map.of(
					"field", error.getField(),
					"message", error.getDefaultMessage()
						))
				.toList();
		return ResponseEntity.badRequest().body(Map.of("errors",errors));
	}
	
	/*
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"IMAGE_TOO_LARGE");
		
		return new ResponseEntity<>(response,HttpStatus.PAYLOAD_TOO_LARGE);
	}
	*/
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"RESOURCE_NOT_FOUND");
		
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ImageValidatorException.class)
	public ResponseEntity<ErrorResponse> handleImageValidatorException(ImageValidatorException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"BAD_REQUEST");
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(DataConflictException.class)
	public ResponseEntity<ErrorResponse> handleDataConflictException(DataConflictException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"DATA_CONFLICT");
		
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(StorageException.class)
	public ResponseEntity<ErrorResponse> handleStorageException(StorageException ex) {
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"STORAGE_SERVICE_ERROR");
		
		return new ResponseEntity<>(response,HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"INVALID_ARGUMENT");
		
		return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"ACCESS_DENIED");
		
		return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"INTERNAL_SERVER_ERROR");
		
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"DATA_INTEGRITY_VIOLATION");
		
		return new ResponseEntity<>(response,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex){
		
		ErrorResponse response = new ErrorResponse(ex.getMessage(),"INTERNAL_SERVER_ERROR");
		
		return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
