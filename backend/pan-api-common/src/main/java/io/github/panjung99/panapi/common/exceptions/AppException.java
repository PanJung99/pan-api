package io.github.panjung99.panapi.common.exceptions;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {

    private final ErrorEnum error;

    public AppException(ErrorEnum error) {
        super(error.getDesc());
        this.error = error;
    }

}
