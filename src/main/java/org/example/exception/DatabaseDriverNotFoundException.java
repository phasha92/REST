package org.example.exception;

public class DatabaseDriverNotFoundException extends RuntimeException {
    public DatabaseDriverNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
