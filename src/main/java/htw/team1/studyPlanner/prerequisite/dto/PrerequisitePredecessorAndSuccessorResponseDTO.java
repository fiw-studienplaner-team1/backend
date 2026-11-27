package htw.team1.studyPlanner.prerequisite.dto;

import htw.team1.studyPlanner.enums.PrerequisiteMode;

public record PrerequisitePredecessorAndSuccessorResponseDTO(
		Long id,
		Long predecessorModuleId, 
		String predecessorModuleNumber, 
		Long succesorModuleId, 
		String successorModuleNumber, 
		PrerequisiteMode prerequisiteMode)
{

}
