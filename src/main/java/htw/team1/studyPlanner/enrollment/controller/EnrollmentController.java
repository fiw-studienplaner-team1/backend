package htw.team1.studyPlanner.enrollment.controller;

import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestDTO;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestMoveDTO;
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
    // eine neue Zeile wird in der Tabelle angelegt
    @PostMapping
    public ResponseEntity<EnrollmentResponseDTO> createEnrollment(@PathVariable Long semesterPlanId,
                                                            @RequestBody EnrollmentRequestDTO request) {
        LOG.info("Modul {} wird in Semesterplan {} eingeplant", request.moduleId(), semesterPlanId);

        EnrollmentResponseDTO enrollment = this.enrollmentService.enrollToSemesterPlan(semesterPlanId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollment);
    }

    // PUT /semesterplans/{semesterPlanId}/enrollments/{enrollmentId}/move
    // eine bestehende Zeile in der Tabelle wird verändert, id bleibt dabei gleich
    @PutMapping("/{enrollmentId}/move")
    //updateEnrollment
    public ResponseEntity<EnrollmentResponseDTO> updateEnrollment(
            @PathVariable Long semesterPlanId,
            @PathVariable Long enrollmentId,
            @RequestBody EnrollmentRequestMoveDTO request) {
        LOG.info("Belegung {} in Semesterplan {} wird nach Semesterplan {} verschoben",
        enrollmentId, semesterPlanId, request == null ? null : request.targetSemesterPlanId());
        EnrollmentResponseDTO enrollment = enrollmentService.moveEnrollment(semesterPlanId, enrollmentId, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(enrollment);
    }

    // DELETE /semesterplans/{semesterPlanId}/enrollments/{enrollmentId}
    @DeleteMapping("/{enrollmentId}")
   //Delete allEnrollments
    public ResponseEntity<Void> deleteEnrollmentByID(
            @PathVariable Long semesterPlanId,
            @PathVariable Long enrollmentId) {

        enrollmentService.deleteEnrollment(semesterPlanId, enrollmentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
