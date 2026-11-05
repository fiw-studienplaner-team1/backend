package htw.team1.studyPlanner.dto;

import jakarta.persistence.Column;

public record ModuleRequestDTO(String id, String name, String semester, int credits) {
}
