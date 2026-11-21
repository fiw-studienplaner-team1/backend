package htw.team1.studyPlanner.semesterplan.dto;

// Was das Frontend beim Anlegen eines Semesterplans schickt
//TODO Die Entity hat targetEcts und targetWeeklyHours, aber das Request-DTO überträgt sie nicht. Die Felder bleiben immer NULL
public record SemesterPlanRequestDTO(Long semesterId) { }
