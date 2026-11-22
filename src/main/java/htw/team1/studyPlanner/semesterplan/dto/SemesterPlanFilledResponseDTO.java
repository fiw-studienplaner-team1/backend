package htw.team1.studyPlanner.semesterplan.dto;

import java.util.List;

// Ein Semesterplan mit Semester und allen eingeplanten Modulen
public record SemesterPlanFilledResponseDTO(Long id, Long semesterId, String semesterLabel, List<PlannedModuleResponseDTO> modules) {
}
