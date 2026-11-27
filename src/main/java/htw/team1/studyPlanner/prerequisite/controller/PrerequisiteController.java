package htw.team1.studyPlanner.prerequisite.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import htw.team1.studyPlanner.prerequisite.dto.PrerequisitePredecessorAndSuccessorResponseDTO;
import htw.team1.studyPlanner.prerequisite.dto.PrerequisiteResponseDTO;
import htw.team1.studyPlanner.prerequisite.service.PrerequisiteService;

@RestController
public class PrerequisiteController {
	
    private final Logger LOG = LoggerFactory.getLogger(getClass());
	
	private PrerequisiteService prerequisiteService;
	
	public PrerequisiteController(PrerequisiteService prerequisiteService)
	{
		this.prerequisiteService = prerequisiteService;
	}
	
	@GetMapping("/predecessors/{moduleNumber}")
	public ResponseEntity<List<PrerequisiteResponseDTO>> getAllPredecessorsOfModuleByModuleNumber(@PathVariable String moduleNumber)
	{
		//TODO anpassen wenn erweitert um nicht-Modul-Vorraussetzungen
        LOG.info("Vorausgesetze Module vom Modul-Nummer {} werden abgerufen", moduleNumber);
		
		//TODO anpassen wenn erweitert um nicht-Modul-Vorraussetzungen
		List<PrerequisiteResponseDTO> predecessors = this.prerequisiteService.findAllPredecessors(moduleNumber);
		return ResponseEntity.status(HttpStatus.OK)
				.body(predecessors);
	}
	
	@GetMapping("/successors/{moduleNumber}")
	public ResponseEntity<List<PrerequisiteResponseDTO>> getAllSuccessorsOfModuleByModulNumber(@PathVariable String moduleNumber)
	{
        LOG.info("Module, die Modul-Nummer {} als Voraussetzung haben, werden abgerufen", moduleNumber);

		List<PrerequisiteResponseDTO> successors = this.prerequisiteService.findAllSuccessors(moduleNumber);
		return ResponseEntity.status(HttpStatus.OK)
				.body(successors);
	}
	
	@GetMapping("/prerequisites")
	public ResponseEntity<List<PrerequisitePredecessorAndSuccessorResponseDTO>> getAllPrerequisites()
	{
		LOG.info("Alle Voraussetzungen von allen Module werden geladen");
		
		List<PrerequisitePredecessorAndSuccessorResponseDTO> prerequisites = this.prerequisiteService.findAllPrerequisites();
		return ResponseEntity.status(HttpStatus.OK)
				.body(prerequisites);
	}
	
}
