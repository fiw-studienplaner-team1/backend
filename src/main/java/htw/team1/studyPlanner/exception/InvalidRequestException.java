package htw.team1.studyPlanner.exception;

// Wird geworfen, wenn die Eingabe fachlich ungueltig ist (z. B. Ziel-Semesterplan existiert
// nicht, Modul wird im Zielsemester nicht angeboten, Request-Body fehlt).
// Wird vom GlobalExceptionHandler auf HTTP 400 gemappt.

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}