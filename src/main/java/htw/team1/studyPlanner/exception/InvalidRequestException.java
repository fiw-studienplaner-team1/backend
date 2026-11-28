package htw.team1.studyPlanner.exception;

//Wird geworfen, wenn die Eingabe ungueltig ist (z. B. Request-Body fehlt oder Quell- und
// Ziel-Semesterplan identisch sind).

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}