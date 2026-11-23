package com.exemple.dao;

public class DonneesException extends RuntimeException {
    public DonneesException(String message, Throwable cause) {
        super(message, cause);
    }
    public DonneesException(String message) {
        super(message);
    }
}