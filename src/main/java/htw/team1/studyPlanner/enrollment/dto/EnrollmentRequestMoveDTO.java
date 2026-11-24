package htw.team1.studyPlanner.enrollment.dto;

// Was das Frontend schickt beim Verschieben einer Belegung in einen anderen Semesterplan
public record EnrollmentRequestMoveDTO(Long targetSemesterPlanId) {
}
