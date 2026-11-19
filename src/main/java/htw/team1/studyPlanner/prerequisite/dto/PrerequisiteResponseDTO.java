package htw.team1.studyPlanner.prerequisite.dto;

import htw.team1.studyPlanner.enums.PrerequisiteMode;

//TODO anpassen wenn erweitert um nicht-Modul-Vorraussetzungen, evtl aufteilen in predecessor und successor
public record PrerequisiteResponseDTO(Long modulPrerequisiteId, PrerequisiteMode mode)
{

}
