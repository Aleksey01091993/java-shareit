package ru.practicum.shareit.exception;

public class InternalServerError500 extends RuntimeException{
    public InternalServerError500(String message) {
        super(message);
    }
}
