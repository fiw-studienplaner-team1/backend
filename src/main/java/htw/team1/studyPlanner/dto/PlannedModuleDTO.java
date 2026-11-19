package htw.team1.studyPlanner.dto;

// Ein eingeplantes Modul innerhalb eines Semesterplans
public record PlannedModuleDTO(Long enrollmentId, String moduleId, String name, Integer ects, Integer sws) {
}