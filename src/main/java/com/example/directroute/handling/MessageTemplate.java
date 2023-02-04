package com.example.directroute.handling;

/**
 * Шаблон сообщения API
 */
public interface MessageTemplate {

    /**
     * @param args аргументы, подставляемые в шаблон текста сообщения
     * @return текст сообщения
     */
    String getText(Object... args);
}
