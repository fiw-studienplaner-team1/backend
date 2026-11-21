package htw.team1.studyPlanner.enrollment.controller;

import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestDTO;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentResponseDTO;
import htw.team1.studyPlanner.enrollment.service.EnrollmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semesterplans/{planId}/enrollments")
public class EnrollmentController
{

    // Logger fuer Meldungen in der Konsole
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // POST /semesterplans/{planId}/enrollments - plant ein Modul in den Plan ein
    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> planModule(@PathVariable Long planId,
                                                            @RequestBody EnrollmentRequestDTO request) {
        LOG.info("Modul {} wird in Semesterplan {} eingeplant", request.moduleId(), planId);

        EnrollmentResponseDTO enrollment = this.enrollmentService.planModule(planId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollment);
    }
}
