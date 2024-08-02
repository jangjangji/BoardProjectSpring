package org.jang.global.exceptions.script;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
public class AlertRedierctException extends AlertException
{
    private String url;
    private String target;

    public AlertRedierctException(String message, String url, HttpStatus status, String target){
        super(message, status);
        target = StringUtils.hasText(target) ? target : "self";
        this.url = url;
        this.target = target;
    }
    public AlertRedierctException(String message, String url, HttpStatus status){
        this(message, url, status, null);
    }
}
