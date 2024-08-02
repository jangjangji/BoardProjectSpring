package org.jang.file.exceptions;

import org.jang.global.exceptions.script.AlertBackException;
import org.springframework.http.HttpStatus;

public class FileNotFountException extends AlertBackException {
    public FileNotFountException() {
        super("NotFound.file", HttpStatus.NOT_FOUND);
        setErrorCode(true);

    }
}
