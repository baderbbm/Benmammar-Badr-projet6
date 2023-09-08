package com.openclassrooms.DataLayerSec.exceptions;

// exception personnalisée
public class InsufficientBalanceException extends RuntimeException {
	public InsufficientBalanceException(String message) {
		super(message);
	}
}