package com.codeapp.blog.exception;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class AppExceptionController extends ResponseEntityExceptionHandler{
	
public AppExceptionController(){
	super();	
}
	
	/**
	 * Handle exceptions for resource unavailable/404
	 * @param response
	 * @param ex
	 * @return ResponseEntity with error message
	 */
	@ExceptionHandler(HttpClientErrorException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND )
    public ResponseEntity<Object> handleAppException(HttpServletResponse response, HttpClientErrorException ex) {
				
        return new ResponseEntity<Object>(String.format("No such resource available. Error Message: %s", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
	
	/* (non-Javadoc)
	 * Handle all requests with invalid mappings
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleNoHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(
		    NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return new ResponseEntity<Object>(String.format("No mapping found. Error message: %s ", ex.getMessage()), HttpStatus.NOT_FOUND);
		}
		
	/* (non-Javadoc)
	 * Handle all other exceptions
	 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal(java.lang.Exception, java.lang.Object, org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus, org.springframework.web.context.request.WebRequest)
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<Object>(String.format("Internal error occured. Error message: %s ", ex.getMessage()),status);
	}
}
