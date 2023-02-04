package com.example.directroute.service.exception;

/**
 * Исключение для оборачивания внутренних checked исключений сервера
 */
public class InternalServerException extends RuntimeException {

    public InternalServerException(Throwable cause) {
        super(cause);
    }

    public InternalServerException(String message) {
        super(message);
    }

    public InternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
