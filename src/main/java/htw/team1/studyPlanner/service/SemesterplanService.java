package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.dto.PlannedModuleDTO;
import htw.team1.studyPlanner.dto.SemesterplanDetailDTO;
import htw.team1.studyPlanner.dto.SemesterplanRequestDTO;
import htw.team1.studyPlanner.dto.SemesterplanResponseDTO;
import htw.team1.studyPlanner.entity.Belegung;
import htw.team1.studyPlanner.entity.ModulTurnus;
import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.Semesterplan;
import htw.team1.studyPlanner.entity.StudyModule;
import htw.team1.studyPlanner.repository.BelegungRepository;
import htw.team1.studyPlanner.repository.SemesterRepository;
import htw.team1.studyPlanner.repository.SemesterplanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

// TODO: Fehlerbehandlung im Team festlegen (ResponseStatusException im Service vs. eigene Exceptions
//       mit zentralem @ControllerAdvice) und dann hier und in den anderen Services einheitlich umsetzen
@Service
public class SemesterplanService {

    // TODO: Durch die eingeloggte Studentin ersetzen, sobald der Login fertig ist
    private static final Long TEST_STUDENT_ID = 1L;

    private final SemesterplanRepository semesterplanRepository;
    private final SemesterRepository semesterRepository;
    private final BelegungRepository belegungRepository;

    public SemesterplanService(SemesterplanRepository semesterplanRepository,
                               SemesterRepository semesterRepository,
                               BelegungRepository belegungRepository) {
        this.semesterplanRepository = semesterplanRepository;
        this.semesterRepository = semesterRepository;
        this.belegungRepository = belegungRepository;
    }

    // Legt fuer die Test-Studentin einen Semesterplan im angegebenen Semester an
    public SemesterplanResponseDTO createPlan(SemesterplanRequestDTO request) {
        // 1. Semester suchen, sonst 404
        Semester semester = semesterRepository.findById(request.semesterId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Semester " + request.semesterId() + " nicht gefunden."));

        // 2. Gibt es schon einen Plan in diesem Semester? Dann 409
        if (semesterplanRepository.existsByUserIdAndSemester(TEST_STUDENT_ID, semester)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Fuer dieses Semester gibt es bereits einen Semesterplan.");
        }

        // 3. Neuen Plan anlegen und speichern
        Semesterplan plan = new Semesterplan();
        plan.setUserId(TEST_STUDENT_ID);
        plan.setSemester(semester);
        Semesterplan saved = semesterplanRepository.save(plan);

        // 4. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    // Laedt alle Semesterplaene der Test-Studentin mit ihren eingeplanten Modulen
    public List<SemesterplanDetailDTO> getPlans() {

        // 1. Alle Plaene der Studentin, sortiert nach Semester (1. Abfrage)
        List<Semesterplan> plans = semesterplanRepository.findByUserIdOrderBySemesterStartAsc(TEST_STUDENT_ID);

        // 2. Keine Plaene? Dann leere Liste statt Fehler
        if (plans.isEmpty()) {
            return List.of();
        }

        // 3. Alle Belegungen dieser Plaene auf einmal laden (2. Abfrage, gegen N+1)
        List<Belegung> enrollments = belegungRepository.findByPlanIn(plans);

        // 4. Pro Plan die passenden Belegungen heraussuchen und das DTO bauen
        List<SemesterplanDetailDTO> result = new ArrayList<>();
        for (Semesterplan plan : plans) {
            List<PlannedModuleDTO> modules = new ArrayList<>();
            for (Belegung enrollment : enrollments) {
                if (enrollment.getPlan().getPlanId().equals(plan.getPlanId())) {
                    modules.add(toPlannedModule(enrollment));
                }
            }
            result.add(new SemesterplanDetailDTO(
                    plan.getPlanId(),
                    plan.getSemester().getSemesterId(),
                    toLabel(plan.getSemester()),
                    modules
            ));
        }
        return result;
    }

    // Wandelt die Entity in das Response-DTO um
    private static SemesterplanResponseDTO toResponse(Semesterplan plan) {
        return new SemesterplanResponseDTO(
                plan.getPlanId(),
                plan.getSemester().getSemesterId(),
                plan.getSemester().getType(),
                plan.getSemester().getYear()
        );
    }

    // Wandelt eine Belegung in ein eingeplantes Modul fuer die Anzeige um
    private static PlannedModuleDTO toPlannedModule(Belegung enrollment) {
        StudyModule module = enrollment.getOffering().getModule();
        return new PlannedModuleDTO(
                enrollment.getEnrollmentId(),
                module.getId(),
                module.getName(),
                module.getEcts(),
                module.getSws()
        );
    }

    // Baut die Bezeichnung des Semesters, z. B. "WiSe 2026/27" oder "SoSe 2026"
    private static String toLabel(Semester semester) {
        if (semester.getType() == ModulTurnus.SOSE) {
            return "SoSe " + semester.getYear();
        }
        int nextYear = (semester.getYear() + 1) % 100;
        return "WiSe " + semester.getYear() + "/" + String.format("%02d", nextYear);
    }
}
