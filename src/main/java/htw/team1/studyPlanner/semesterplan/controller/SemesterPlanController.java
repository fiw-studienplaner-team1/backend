package htw.team1.studyPlanner.semesterplan.controller;

import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanFilledResponseDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanEmptyRequestDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanCreateResponseDTO;
import htw.team1.studyPlanner.semesterplan.service.SemesterPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semesterplans")
public class SemesterPlanController
{

    // Logger fuer Meldungen in der Konsole
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final SemesterPlanService semesterPlanService;

    public SemesterPlanController(SemesterPlanService semesterPlanService) {
        this.semesterPlanService = semesterPlanService;
    }

    @PostMapping
    public ResponseEntity<SemesterPlanCreateResponseDTO> createSemesterPlan(@RequestBody SemesterPlanEmptyRequestDTO request) {
        LOG.info("Semesterplan fuer Semester {} wird angelegt", request.semesterId());

        SemesterPlanCreateResponseDTO plan = this.semesterPlanService.createSemsterPlan(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(plan);

    }
    // GET /semesterplans - laedt alle Semesterplaene der Test-Studentin mit ihren Modulen
    @GetMapping
    public ResponseEntity<List<SemesterPlanFilledResponseDTO>> getAllSemesterPlans() {
        LOG.info("Alle Semesterplaene werden abgerufen");

        List<SemesterPlanFilledResponseDTO> plans = this.semesterPlanService.getSemesterPlans();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(plans);
    }
}
