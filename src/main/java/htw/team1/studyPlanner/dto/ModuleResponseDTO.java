package htw.team1.studyPlanner.dto;

import htw.team1.studyPlanner.entity.ModulArt;
import htw.team1.studyPlanner.entity.ModulTurnus;

public record ModuleResponseDTO(String id, String name, int semester, int ects, int sws, ModulArt art, ModulTurnus turnus) {
}
