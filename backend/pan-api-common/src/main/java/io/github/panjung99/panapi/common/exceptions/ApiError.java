package io.github.panjung99.panapi.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class ApiError {
    private final String code;
    private final String type;
    private final String param;
}
