package com.example.tellmystory.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.tellmystory.exception.AccountExistsException;
import com.example.tellmystory.exception.DataNotFoundException;

@RestControllerAdvice
public class ExceptionHandler {

	@org.springframework.web.bind.annotation.ExceptionHandler(DataNotFoundException.class)

	public ResponseEntity<?> accountNotFound(DataNotFoundException dataNotFoundException) {
		return ResponseEntity.badRequest().body(dataNotFoundException.getMessage());

	}

	@org.springframework.web.bind.annotation.ExceptionHandler(AccountExistsException.class)

	public ResponseEntity<?> accountPresent(AccountExistsException accountExistsException) {
		return ResponseEntity.badRequest().body(accountExistsException.getMessage());

	}

}
