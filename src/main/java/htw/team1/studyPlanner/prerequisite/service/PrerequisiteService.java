package htw.team1.studyPlanner.prerequisite.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import htw.team1.studyPlanner.prerequisite.dto.PrerequisiteResponseDTO;
import htw.team1.studyPlanner.prerequisite.entity.Prerequisite;
import htw.team1.studyPlanner.prerequisite.repository.PrerequisiteRepository;

@Service
public class PrerequisiteService
{
	private final PrerequisiteRepository prerequisiteRepository;

	public PrerequisiteService(PrerequisiteRepository prerequisiteRepository)
	{
		this.prerequisiteRepository = prerequisiteRepository;
	}
	
	public List<PrerequisiteResponseDTO> findAllPredecessors(String id) {
		List<Prerequisite> result = findPredecessorsByModulIdOrThrow(id);
		return result.stream()
				.map(PrerequisiteService::prerequisiteToResponse)
				.toList();
	}
	
	public List<PrerequisiteResponseDTO> findAllSuccessors(String id) {
		List<Prerequisite> result = findSuccessorsByModulIdOrThrow(id);
		return result.stream()
				.map(PrerequisiteService::successorToResponse)
				.toList();
	}
	
	private List<Prerequisite> findPredecessorsByModulIdOrThrow(String moduleId) {
		return prerequisiteRepository
		 .findBySuccessorModuleId(moduleId)
		 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + moduleId + " hat keine Vorraussetzungen."));	 
	}

	private List<Prerequisite> findSuccessorsByModulIdOrThrow(String moduleId) {
		return prerequisiteRepository
		 .findByPredecessorModuleId(moduleId)
		 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + moduleId + " ist keine Vorraussetzung für andere Module."));	 
	}
	
	
	public static PrerequisiteResponseDTO prerequisiteToResponse(Prerequisite prerequisite)
	{
		return new PrerequisiteResponseDTO(
				prerequisite.getPredecessorModule().getId(), 
				prerequisite.getMode());
	}
	
	public static PrerequisiteResponseDTO successorToResponse(Prerequisite prerequisite)
	{
		return new PrerequisiteResponseDTO(
				prerequisite.getSuccessorModule().getId(), 
				prerequisite.getMode());
	}
}
