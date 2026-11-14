package htw.team1.studyPlanner.dto;

import htw.team1.studyPlanner.entity.ModulArt;

public record ModuleResponseDTO(String id, String name, int semester, int ects, int sws, ModulArt art, String turnus) {
}
