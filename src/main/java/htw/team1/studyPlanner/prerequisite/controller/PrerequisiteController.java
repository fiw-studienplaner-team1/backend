package htw.team1.studyPlanner.prerequisite.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

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
	
	@GetMapping("/predecessor/{id}")
	public ResponseEntity<List<PrerequisiteResponseDTO>> getAllPredecessorsOfAModule(@PathVariable String id)
	{
		//TODO anpassen wenn erweitert um nicht-Modul-Vorraussetzungen
        LOG.info("Vorrausgesetze Module vom Modul id {} werden abgerufen", id);
		
		//TODO anpassen wenn erweitert um nicht-Modul-Vorraussetzungen
		List<PrerequisiteResponseDTO> predecessors = this.prerequisiteService.findAllPredecessors(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(predecessors);
	}
	
	@GetMapping("/successor/{id}")
	public ResponseEntity<List<PrerequisiteResponseDTO>> getAllSuccessorsOfAModule(@PathVariable String id)
	{
        LOG.info("Module, die Modul id {} als Vorraussetzung haben, werden abgerufen", id);

		List<PrerequisiteResponseDTO> successors = this.prerequisiteService.findAllSuccessors(id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(successors);
	}
	
}
