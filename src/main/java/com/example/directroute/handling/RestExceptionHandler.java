package com.example.directroute.handling;

import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String DEFAULT_SERVER_ERROR_MESSAGE = "Ошибка.";

    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<MessageWithCode> handleException(Throwable t) {
        log.error(t);
        MessageWithCode body = new MessageWithCode(UUID.randomUUID().toString(), DEFAULT_SERVER_ERROR_MESSAGE);
        log.error(body);
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
