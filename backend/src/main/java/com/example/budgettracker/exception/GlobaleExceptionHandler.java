package com.example.budgettracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobaleExceptionHandler {

    @ExceptionHandler(TransaktionNichtGefundenException.class)
    public ResponseEntity<String> handleTransaktionNichtGefundenException(TransaktionNichtGefundenException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(TransaktionVerarbeitenFehlgeschlagenException.class)
    public ResponseEntity<String> handleTransaktionVerarbeitenFehlgeschlagenException(TransaktionVerarbeitenFehlgeschlagenException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(TransaktionLoeschenFehlgeschlagenException.class)
    public ResponseEntity<String> handleTransaktionLoeschenFehlgeschlagenException(TransaktionLoeschenFehlgeschlagenException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
