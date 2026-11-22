package htw.team1.studyPlanner.semesterplan.service;

import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanFilledResponseDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanEmptyRequestDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanCreateResponseDTO;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.semesterplan.repository.SemesterPlanRepository;
import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.semester.repository.SemesterRepository;
import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentResponseDTO;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.enrollment.repository.EnrollmentRepository;
import htw.team1.studyPlanner.enrollment.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

// TODO: Fehlerbehandlung im Team festlegen (ResponseStatusException im Service vs. eigene Exceptions
//       mit zentralem @ControllerAdvice) und dann hier und in den anderen Services einheitlich umsetzen
@Service
public class SemesterPlanService
{

    // TODO: Durch die eingeloggte Studentin ersetzen, sobald der Login fertig ist
    private static final Long TEST_STUDENT_ID = 1L;

    private final SemesterPlanRepository semesterPlanRepository;
    private final SemesterRepository semesterRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SemesterPlanService(SemesterPlanRepository semesterPlanRepository,
                               SemesterRepository semesterRepository,
                               EnrollmentRepository enrollmentRepository) {
        this.semesterPlanRepository = semesterPlanRepository;
        this.semesterRepository = semesterRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    // Legt fuer die Test-Studentin einen Semesterplan im angegebenen Semester an
    public SemesterPlanCreateResponseDTO createSemsterPlan(SemesterPlanEmptyRequestDTO request) {
        // 1. Semester suchen, sonst 404
        Semester semester = semesterRepository.findById(request.semesterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Semester " + request.semesterId() + " nicht gefunden."));

        // 2. Gibt es schon einen Plan in diesem Semester? Dann 409
        if (semesterPlanRepository.existsByUserAndSemesterId(TEST_STUDENT_ID, semester.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Fuer dieses Semester gibt es bereits einen Semesterplan.");
        }

        // 3. Neuen Plan anlegen und speichern
        SemesterPlan semesterPlan = new SemesterPlan();
        semesterPlan.setUser(TEST_STUDENT_ID);
        semesterPlan.setSemester(semester);
        SemesterPlan saved = semesterPlanRepository.save(semesterPlan);

        // 4. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    // Laedt alle Semesterplaene der Test-Studentin mit ihren eingeplanten Modulen
    public List<SemesterPlanFilledResponseDTO> getSemesterPlans() {

        // 1. Alle Plaene der Studentin, sortiert nach Semester (1. Abfrage)
        List<SemesterPlan> semesterPlans = semesterPlanRepository.findByUserOrderBySemesterStartDateAsc(TEST_STUDENT_ID);

        // 2. Keine Plaene? Dann leere Liste statt Fehler
        if (semesterPlans.isEmpty()) {
            return List.of();
        }

        // 3. Alle Belegungen dieser Plaene auf einmal laden (2. Abfrage, gegen N+1)
        List<Enrollment> enrollments = enrollmentRepository.findBySemesterPlanIn(semesterPlans);

        // 4. Pro Plan die passenden Belegungen heraussuchen und das DTO bauen
        List<SemesterPlanFilledResponseDTO> result = new ArrayList<>();
        for (SemesterPlan semesterPlan : semesterPlans) {
            List<EnrollmentResponseDTO> modules = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getSemesterPlan().getId().equals(semesterPlan.getId())) {
                    modules.add(EnrollmentService.toResponse(enrollment));
                }
            }
            result.add(new SemesterPlanFilledResponseDTO(
                    semesterPlan.getId(),
                    semesterPlan.getSemester().getId(),
                    toLabel(semesterPlan.getSemester()),
                    modules
            ));
        }
        return result;
    }

    // Wandelt die Entity in das Response-DTO um
    private static SemesterPlanCreateResponseDTO toResponse(SemesterPlan semesterPlan) {
        return new SemesterPlanCreateResponseDTO(
                semesterPlan.getId(),
                semesterPlan.getSemester().getId(),
                semesterPlan.getSemester().getCycle(),
                semesterPlan.getSemester().getYear()
        );
    }


    // Baut die Bezeichnung des Semesters, z. B. "WiSe 2026/27" oder "SoSe 2026"
    private static String toLabel(Semester semester) {
        if (semester.getCycle() == Cycle.SOSE) {
            return "SoSe " + semester.getYear();
        }
        int nextYear = (semester.getYear() + 1) % 100;
        return "WiSe " + semester.getYear() + "/" + String.format("%02d", nextYear);
    }
}
