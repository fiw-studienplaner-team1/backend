package htw.team1.studyPlanner.exception;

import java.time.Instant;

//Einheitliches Format fuer Fehlerantworten des GlobalExceptionHandler

public record ErrorResponse (Instant timestamp, int status, String error, String message, String path) {

}
