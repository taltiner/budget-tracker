package com.example.budgettracker.exception;

public class TransaktionVerarbeitenFehlgeschlagenException extends RuntimeException {
    private static final String ERROR_MESSAGE = "Es ist ein Fehler beim Verarbeiten der Transaktion aufgetretten.";

    public TransaktionVerarbeitenFehlgeschlagenException() {
        super(ERROR_MESSAGE);
    }

    public TransaktionVerarbeitenFehlgeschlagenException(Throwable cause) {
        super(ERROR_MESSAGE, cause);
    }

    public TransaktionVerarbeitenFehlgeschlagenException(String customMessage, Throwable cause) {
        super(customMessage, cause);
    }
}
