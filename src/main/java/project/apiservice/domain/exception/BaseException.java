package project.apiservice.domain.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final String identifier;

    public BaseException(String identifier) {
        this.identifier = identifier;
    }
}
