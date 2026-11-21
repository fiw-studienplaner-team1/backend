package htw.team1.studyPlanner.semesterplan.service;

import htw.team1.studyPlanner.semesterplan.dto.PlannedModuleDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanDetailDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanRequestDTO;
import htw.team1.studyPlanner.semesterplan.dto.SemesterPlanResponseDTO;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.semesterplan.repository.SemesterPlanRepository;
import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.semester.repository.SemesterRepository;
import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.enrollment.repository.EnrollmentRepository;
import htw.team1.studyPlanner.module.entity.StudyModule;
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
    public SemesterPlanResponseDTO createPlan(SemesterPlanRequestDTO request) {
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
        SemesterPlan plan = new SemesterPlan();
        plan.setUser(TEST_STUDENT_ID);
        plan.setSemester(semester);
        SemesterPlan saved = semesterPlanRepository.save(plan);

        // 4. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    // Laedt alle Semesterplaene der Test-Studentin mit ihren eingeplanten Modulen
    public List<SemesterPlanDetailDTO> getPlans() {

        // 1. Alle Plaene der Studentin, sortiert nach Semester (1. Abfrage)
        List<SemesterPlan> plans = semesterPlanRepository.findByUserOrderBySemesterStartDateAsc(TEST_STUDENT_ID);

        // 2. Keine Plaene? Dann leere Liste statt Fehler
        if (plans.isEmpty()) {
            return List.of();
        }

        // 3. Alle Belegungen dieser Plaene auf einmal laden (2. Abfrage, gegen N+1)
        List<Enrollment> enrollments = enrollmentRepository.findByPlanIn(plans);

        // 4. Pro Plan die passenden Belegungen heraussuchen und das DTO bauen
        List<SemesterPlanDetailDTO> result = new ArrayList<>();
        for (SemesterPlan plan : plans) {
            List<PlannedModuleDTO> modules = new ArrayList<>();
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getPlan().getId().equals(plan.getId())) {
                    modules.add(toPlannedModule(enrollment));
                }
            }
            result.add(new SemesterPlanDetailDTO(
                    plan.getId(),
                    plan.getSemester().getId(),
                    toLabel(plan.getSemester()),
                    modules
            ));
        }
        return result;
    }

    // Wandelt die Entity in das Response-DTO um
    private static SemesterPlanResponseDTO toResponse(SemesterPlan plan) {
        return new SemesterPlanResponseDTO(
                plan.getId(),
                plan.getSemester().getId(),
                plan.getSemester().getCycle(),
                plan.getSemester().getYear()
        );
    }

    // Wandelt eine Belegung in ein eingeplantes Modul fuer die Anzeige um
    private static PlannedModuleDTO toPlannedModule(Enrollment enrollment) {
        StudyModule module = enrollment.getModuleOffering().getModule();
        return new PlannedModuleDTO(
                enrollment.getId(),
                module.getId(),
                module.getName(),
                module.getEcts(),
                module.getWeeklyHours()
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
