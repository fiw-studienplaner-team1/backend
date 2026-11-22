package htw.team1.studyPlanner.semesterplan.dto;

import htw.team1.studyPlanner.enums.Cycle;

// Was nach dem Anlegen an das Frontend zurueckgeht
public record SemesterPlanCreateResponseDTO(Long id, Long semesterId, Cycle cycle, Integer year) { }
