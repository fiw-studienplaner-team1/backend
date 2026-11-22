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
@RequestMapping("/semesterplans/{semesterPlanId}/enrollments")
public class EnrollmentController
{

    // Logger fuer Meldungen in der Konsole
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // POST /semesterplans/{semesterPlanId}/enrollments - plant ein Modul in den Plan ein
    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(@PathVariable Long semesterPlanId,
                                                            @RequestBody EnrollmentRequestDTO request) {
        LOG.info("Modul {} wird in Semesterplan {} eingeplant", request.moduleId(), semesterPlanId);

        EnrollmentResponseDTO enrollment = this.enrollmentService.enrollToSemesterPlan(semesterPlanId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollment);
    }
}
