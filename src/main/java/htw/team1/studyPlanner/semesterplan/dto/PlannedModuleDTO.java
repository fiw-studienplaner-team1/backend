package htw.team1.studyPlanner.semesterplan.dto;

// Ein eingeplantes Modul innerhalb eines Semesterplans
public record PlannedModuleDTO(Long enrollmentId, Long moduleId, String name, Integer ects, Integer weeklyHours) {
}
