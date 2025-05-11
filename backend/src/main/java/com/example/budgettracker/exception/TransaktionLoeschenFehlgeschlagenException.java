package com.example.budgettracker.exception;

public class TransaktionLoeschenFehlgeschlagenException extends RuntimeException {

    public TransaktionLoeschenFehlgeschlagenException() {
        super();
    }

    public TransaktionLoeschenFehlgeschlagenException(String message) {
        super(message);
    }

    public TransaktionLoeschenFehlgeschlagenException(String message, Throwable cause) {
        super(message, cause);
    }
}
