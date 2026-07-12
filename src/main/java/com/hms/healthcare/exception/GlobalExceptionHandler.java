package com.hms.healthcare.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,Object> handleValidationExceptions(MethodArgumentNotValidException ex)
	{
		Map<String,String> errors=new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach( error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});
		return Map.of("error",errors);
	}
	
	
	
	  @ExceptionHandler(BadCredentialsException.class) 
	  @ResponseStatus(HttpStatus.UNAUTHORIZED) 
	  public Map<String,Object> handleBadCredentialsException(BadCredentialsException ex) { 
		  return Map.of("error","Invalid Password"); 
	  }
	 
	
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String,Object> handleDataNotFoundException(DataNotFoundException ex)
	{
		return Map.of("error",ex.getMessage());
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,Object> handle(HttpMessageNotReadableException ex)
	{
		return Map.of("error","Enter data in correct format");
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Map<String,Object> handle(IllegalArgumentException ex)
	{
		return Map.of("error",ex.getMessage());
	}
	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Map<String,Object> handle(NoResourceFoundException ex)
	{
		return Map.of("error","You Have Entered Wrong URL ");
	}
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public Map<String,Object> handle(HttpRequestMethodNotSupportedException ex)
	{
		return Map.of("error","Select proper Http Method");
	}
	@ExceptionHandler(DisabledException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String,Object> handle(DisabledException ex)
	{
		return Map.of("error","Your Account is Blocked Contact Admin");
	}
	@ExceptionHandler(ExpiredJwtException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String,Object> handle(ExpiredJwtException ex)
	{
		return Map.of("error","Token Expired");
	}
	@ExceptionHandler(MalformedJwtException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Map<String,Object> handle(MalformedJwtException ex)
	{
		return Map.of("error","Invalid Token");
	}
	
}
