package htw.team1.studyPlanner.dto;

// Was nach dem Einplanen an das Frontend zurueckgeht
public record BelegungResponseDTO(Long enrollmentId, Long planId, Long offeringId, String moduleId, String moduleName) {
}