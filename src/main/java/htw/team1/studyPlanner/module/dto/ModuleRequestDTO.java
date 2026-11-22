package htw.team1.studyPlanner.module.dto;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enums.ModuleForm;
import htw.team1.studyPlanner.enums.ModuleType;

public record ModuleRequestDTO(Long id,
                               String name,
                               String moduleNumber,
                               Integer recommendedSemester,
                               Integer ects,
                               Integer weeklyHours,
                               ModuleType moduleType,
                               Cycle cycle,
                               ModuleForm moduleForm)
    //TODO welche von den attributen sollten wir vom frontend veraenderbar machen und welche nicht (die dann hier raus)
{ }
