package htw.team1.studyPlanner.dto;

import java.util.List;

// Ein Semesterplan mit Semester und allen eingeplanten Modulen
public record SemesterplanDetailDTO(Long planId, Long semesterId, String semesterLabel, List<PlannedModuleDTO> modules) {
}