package com.openclassrooms.DataLayerSec.exceptions;

// exception personnalisée
public class EmailExistsException extends RuntimeException {
	public EmailExistsException(String message) {
		super(message);
	}
}
