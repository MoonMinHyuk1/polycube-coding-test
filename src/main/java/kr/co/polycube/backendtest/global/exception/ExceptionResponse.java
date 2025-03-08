package kr.co.polycube.backendtest.global.exception;

import lombok.Builder;

@Builder
public record ExceptionResponse(
        String reason
) {
}
