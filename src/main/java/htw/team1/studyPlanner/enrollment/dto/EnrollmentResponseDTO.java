package htw.team1.studyPlanner.enrollment.dto;

// Was nach dem Einplanen an das Frontend zurueckgeht
public record EnrollmentResponseDTO(Long id,
                                    Long planId,
                                    Long moduleOfferingId,
                                    Long moduleId,
                                    String moduleName) { }
