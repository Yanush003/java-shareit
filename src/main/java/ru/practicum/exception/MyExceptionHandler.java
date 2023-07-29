package ru.practicum.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class MyExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<?> handlerDuplicateEmailException(DuplicateEmailException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("");
    }

    @ExceptionHandler(ValidateEmailException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(ValidateEmailException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerIllegalArgumentException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<String> handleWrongDataException(WrongDataException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
