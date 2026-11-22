package htw.team1.studyPlanner.enrollment.dto;

// Was nach dem Einplanen an das Frontend zurueckgeht (wird auch in Semesterplan-Listen verwendet)
public record EnrollmentResponseDTO(Long id,
                                    Long semesterPlanId,
                                    Long moduleOfferingId,
                                    Long moduleId,
                                    String moduleName,
                                    Integer ects,
                                    Integer weeklyHours) { }
