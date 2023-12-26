package ru.practicum.shareit.library.api.exception;

public class BadRequestParamException extends RuntimeException {
    public BadRequestParamException(String message) {
        super(message);
    }
}
