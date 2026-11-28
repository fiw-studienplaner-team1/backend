package htw.team1.studyPlanner.exception;

// Wird geworfen, wenn eine referenzierte Ressource (Semesterplan, Belegung, ...) nicht existiert.
// Wird vom GlobalExceptionHandler auf HTTP 404 gemappt.

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
