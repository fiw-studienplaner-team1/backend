package htw.team1.studyPlanner.semesterplan.dto;

import htw.team1.studyPlanner.enrollment.dto.EnrollmentResponseDTO;

import java.util.List;

// Ein Semesterplan mit Semester und allen eingeplanten Modulen
public record SemesterPlanFilledResponseDTO(Long id, Long semesterId, String semesterLabel, List<EnrollmentResponseDTO> modules) {
}
