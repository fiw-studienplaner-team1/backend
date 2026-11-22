package htw.team1.studyPlanner.semesterplan.dto;

// Was das Frontend beim Anlegen eines Semesterplans schickt
//TODO ist NOCH empty, aber die Entity hat targetEcts und targetWeeklyHours, aber das Request-DTO überträgt sie nicht
public record SemesterPlanEmptyRequestDTO(Long semesterId) { }

