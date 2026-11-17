package htw.team1.studyPlanner.controller;

import htw.team1.studyPlanner.dto.BelegungRequestDTO;
import htw.team1.studyPlanner.dto.BelegungResponseDTO;
import htw.team1.studyPlanner.service.BelegungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semesterplans/{planId}/enrollments")
public class BelegungController {

    // Logger fuer Meldungen in der Konsole
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final BelegungService belegungService;

    public BelegungController(BelegungService belegungService) {
        this.belegungService = belegungService;
    }

    // POST /semesterplans/{planId}/enrollments - plant ein Modul in den Plan ein
    @PostMapping
    public ResponseEntity<BelegungResponseDTO> planModule(@PathVariable Long planId,
                                                          @RequestBody BelegungRequestDTO request) {
        LOG.info("Modul {} wird in Semesterplan {} eingeplant", request.moduleId(), planId);

        BelegungResponseDTO enrollment = this.belegungService.planModule(planId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollment);
    }
}