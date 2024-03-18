package ru.practicum.exception;

public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}
