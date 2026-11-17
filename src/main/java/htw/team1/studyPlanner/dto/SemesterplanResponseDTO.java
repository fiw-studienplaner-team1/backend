package htw.team1.studyPlanner.dto;

import htw.team1.studyPlanner.entity.ModulTurnus;

// Was nach dem Anlegen an das Frontend zurueckgeht
public record SemesterplanResponseDTO(Long planId, Long semesterId, ModulTurnus type, Integer year) {
}