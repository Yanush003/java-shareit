package ru.practicum.exception;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@RequiredArgsConstructor
public class MyExceptionHandler {
    @ExceptionHandler(InternalServerErrorException.class)
    @Generated
    public ResponseEntity<?> handlerDuplicateEmailException(InternalServerErrorException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                "{\n\"error\": \"" + ex.getMessage() + "\"\n}");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @Generated
    public ResponseEntity<?> handlerIllegalArgumentException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
    }

    @ExceptionHandler(WrongDataException.class)
    @Generated
    public ResponseEntity<String> handleWrongDataException(WrongDataException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
