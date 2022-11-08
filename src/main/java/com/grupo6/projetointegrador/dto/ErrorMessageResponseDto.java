package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class ErrorMessageResponseDto {
    private final List<ErrorMessageDto> errors;

    public static ErrorMessageResponseDto of(List<ErrorMessageDto> errors) {
        return new ErrorMessageResponseDto(errors);
    }

    public static ErrorMessageResponseDto of(String message, String errorType) {
        return ErrorMessageResponseDto.of(
                List.of(
                        ErrorMessageDto.of(message, errorType)
                )
        );
    }

    public static ErrorMessageResponseDto withFieldErrors(List<FieldError> fieldErrors) {
        return ErrorMessageResponseDto.of(
                fieldErrors.stream()
                        .map(fieldError -> ErrorMessageDto.of(
                                fieldError.getDefaultMessage(), fieldError.getCode(), fieldError.getField()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
