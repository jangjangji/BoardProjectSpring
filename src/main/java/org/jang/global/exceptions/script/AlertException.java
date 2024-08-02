package org.jang.global.exceptions.script;

import org.jang.global.exceptions.CommonException;
import org.springframework.http.HttpStatus;

public class AlertException extends CommonException {

    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }
}
