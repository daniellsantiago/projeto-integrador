package com.grupo6.projetointegrador.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
@AllArgsConstructor
public class ErrorMessageDto {
    private final String message;
    private final String errorType;
    private String field;

    public static ErrorMessageDto of(String message, String errorType) {
        return new ErrorMessageDto(message, errorType);
    }

    public static ErrorMessageDto of(String message, String errorType, String field) {
        return new ErrorMessageDto(message, errorType, field);
    }
}
