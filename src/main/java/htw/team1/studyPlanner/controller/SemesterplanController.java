package htw.team1.studyPlanner.controller;

import htw.team1.studyPlanner.dto.SemesterplanDetailDTO;
import htw.team1.studyPlanner.dto.SemesterplanRequestDTO;
import htw.team1.studyPlanner.dto.SemesterplanResponseDTO;
import htw.team1.studyPlanner.service.SemesterplanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semesterplans")
public class SemesterplanController {

    // Logger fuer Meldungen in der Konsole
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final SemesterplanService semesterplanService;

    public SemesterplanController(SemesterplanService semesterplanService) {
        this.semesterplanService = semesterplanService;
    }

    @PostMapping
    public ResponseEntity<SemesterplanResponseDTO> createPlan(@RequestBody SemesterplanRequestDTO request) {
        LOG.info("Semesterplan fuer Semester {} wird angelegt", request.semesterId());

        SemesterplanResponseDTO plan = this.semesterplanService.createPlan(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(plan);

    }
    // GET /semesterplans - laedt alle Semesterplaene der Test-Studentin mit ihren Modulen
    @GetMapping
    public ResponseEntity<List<SemesterplanDetailDTO>> getPlans() {
        LOG.info("Alle Semesterplaene werden abgerufen");

        List<SemesterplanDetailDTO> plans = this.semesterplanService.getPlans();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(plans);
    }
}