package com.example.tellmystory.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class DataNotFoundException extends RuntimeException {

	private final String message;

}
