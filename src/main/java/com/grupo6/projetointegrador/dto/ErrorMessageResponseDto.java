package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.FieldError;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public static ErrorMessageResponseDto withFieldErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return ErrorMessageResponseDto.of(
                constraintViolations.stream()
                        .map(constraintViolation -> ErrorMessageDto.of(
                                constraintViolation.getMessage(),
                                "FIELD_VALIDATION_ERROR",
                                Objects.requireNonNull(
                                        StreamSupport.stream(
                                                        constraintViolation.getPropertyPath().spliterator(), false
                                                )
                                                .reduce((first, second) -> second)
                                                .orElse(null)).toString()
                        ))
                        .collect(Collectors.toList())
        );
    }
}
