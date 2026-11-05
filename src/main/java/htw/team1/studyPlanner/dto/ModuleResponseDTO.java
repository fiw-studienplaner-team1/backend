package htw.team1.studyPlanner.dto;

import jakarta.persistence.Column;

public record ModuleResponseDTO(String id,String name, String semester, int credits) {
}
