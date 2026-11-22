package htw.team1.studyPlanner.semesterplan.dto;


// Ein eingeplantes Modul innerhalb eines Semesterplans
public record PlannedModuleResponseDTO(Long enrollmentId, Long moduleId, String moduleName, Integer ects, Integer weeklyHours) {
}
