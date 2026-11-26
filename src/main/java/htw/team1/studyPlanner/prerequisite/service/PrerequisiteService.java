package htw.team1.studyPlanner.prerequisite.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import htw.team1.studyPlanner.prerequisite.dto.PrerequisitePredecessorAndSuccessorResponseDTO;
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
	
	public List<PrerequisiteResponseDTO> findAllPredecessors(String moduleNumber) {
		List<Prerequisite> result = findPredecessorsByModulNumberOrThrow(moduleNumber);
		return result.stream()
				.map(PrerequisiteService::predecessorToResponse)
				.toList();
	}
	
	public List<PrerequisiteResponseDTO> findAllSuccessors(String moduleNumber) {
		List<Prerequisite> result = findSuccessorsByModulNumberOrThrow(moduleNumber);
		return result.stream()
				.map(PrerequisiteService::successorToResponse)
				.toList();
	}
	
	public List<PrerequisitePredecessorAndSuccessorResponseDTO> findAllPrerequisites()
	{
		return prerequisiteRepository.findAll().stream()
				.map(PrerequisiteService::prerequisiteToResponse)
				.toList();
	}
	
	private List<Prerequisite> findPredecessorsByModulNumberOrThrow(String moduleNumber) {
		return prerequisiteRepository
		 .findBySuccessorModuleNumber(moduleNumber)
		 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + moduleNumber + " hat keine Vorraussetzungen."));	 
	}

	private List<Prerequisite> findSuccessorsByModulNumberOrThrow(String moduleNumber) {
		return prerequisiteRepository
		 .findByPredecessorModuleId(moduleNumber)
		 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + moduleNumber + " ist keine Vorraussetzung für andere Module."));	 
	}
	
	public static PrerequisiteResponseDTO predecessorToResponse(Prerequisite prerequisite)
	{
		return new PrerequisiteResponseDTO(
				prerequisite.getPredecessorModule().getId(),
				prerequisite.getPredecessorModule().getModuleNumber(),
				prerequisite.getPrerequisiteMode());
	}
	
	public static PrerequisiteResponseDTO successorToResponse(Prerequisite prerequisite)
	{
		return new PrerequisiteResponseDTO(
				prerequisite.getSuccessorModule().getId(),
				prerequisite.getSuccessorModule().getModuleNumber(),
				prerequisite.getPrerequisiteMode());
	}
	
	public static PrerequisitePredecessorAndSuccessorResponseDTO prerequisiteToResponse(Prerequisite prerequisite)
	{
		return new PrerequisitePredecessorAndSuccessorResponseDTO(
				prerequisite.getId(),
				prerequisite.getPredecessorModule().getId(),
				prerequisite.getPredecessorModule().getModuleNumber(),
				prerequisite.getSuccessorModule().getId(),
				prerequisite.getSuccessorModule().getModuleNumber(),
				prerequisite.getPrerequisiteMode()
				);
	}
}
