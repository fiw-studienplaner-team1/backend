package htw.team1.studyPlanner.semesterplan.controller;

import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanDetailDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanRequestDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanResponseDTO;
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

    private final SemesterPlanService semesterplanService;

    public SemesterPlanController(SemesterPlanService semesterplanService) {
        this.semesterplanService = semesterplanService;
    }

    @PostMapping
    public ResponseEntity<SemesterPlanResponseDTO> createPlan(@RequestBody SemesterPlanRequestDTO request) {
        LOG.info("Semesterplan fuer Semester {} wird angelegt", request.semesterId());

        SemesterPlanResponseDTO plan = this.semesterplanService.createPlan(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(plan);

    }
    // GET /semesterplans - laedt alle Semesterplaene der Test-Studentin mit ihren Modulen
    @GetMapping
    public ResponseEntity<List<SemesterPlanDetailDTO>> getPlans() {
        LOG.info("Alle Semesterplaene werden abgerufen");

        List<SemesterPlanDetailDTO> plans = this.semesterplanService.getPlans();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(plans);
    }
}
