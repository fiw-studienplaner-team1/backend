package htw.team1.studyPlanner.module.dto;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enums.ModuleForm;
import htw.team1.studyPlanner.enums.ModuleType;

public record ModuleResponseDTO(Long id,
                                String name,
                                String moduleNumber,
                                Integer recommendedSemester,
                                Integer ects,
                                Integer weeklyHours,
                                ModuleType moduleType,
                                Cycle cycle,
                                ModuleForm moduleForm)
{ }
