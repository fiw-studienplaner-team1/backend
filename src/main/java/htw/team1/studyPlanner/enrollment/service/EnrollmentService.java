package htw.team1.studyPlanner.enrollment.service;

import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestDTO;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestMoveDTO;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentResponseDTO;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.enrollment.repository.EnrollmentRepository;
import htw.team1.studyPlanner.exception.InvalidRequestException;
import htw.team1.studyPlanner.exception.ResourceNotFoundException;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.semesterplan.repository.SemesterPlanRepository;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.module.repository.ModuleOfferingRepository;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// TODO: Fehlerbehandlung im Team festlegen (ResponseStatusException im Service vs. eigene Exceptions
//       mit zentralem @ControllerAdvice) und dann hier und in den anderen Services einheitlich umsetzen -
//       Da beschlossen, im neu hinzugefuegten Code Globaler Ansatz
@Service
public class EnrollmentService
{

    private final EnrollmentRepository enrollmentRepository;
    private final SemesterPlanRepository semesterPlanRepository;
    private final ModuleRepository moduleRepository;
    private final ModuleOfferingRepository moduleOfferingRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             SemesterPlanRepository semesterPlanRepository,
                             ModuleRepository moduleRepository,
                             ModuleOfferingRepository moduleOfferingRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.semesterPlanRepository = semesterPlanRepository;
        this.moduleRepository = moduleRepository;
        this.moduleOfferingRepository = moduleOfferingRepository;
    }

    //TODO: hat Keine ECTS/SWS-Validierung
    //Wenn ein Modul in einen Plan eingeplant wird, wird nicht geprueft, ob die ECTS- oder SWS-Grenze ueberschritten wird.
    // Die Entity hat targetEcts und targetWeeklyHours, aber diese werden nie geprueft.

    // Schreibt ein Modul in einen Semesterplan ein (legt eine Belegung an)
    public EnrollmentResponseDTO enrollToSemesterPlan(Long semesterPlanId, EnrollmentRequestDTO request) {

        // 1. Plan suchen, sonst 404
        SemesterPlan semesterPlan = semesterPlanRepository.findById(semesterPlanId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semesterplan " + semesterPlanId + " nicht gefunden."));

        // 2. Modul suchen, sonst 404
        StudyModule module = moduleRepository.findById(request.moduleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Modul " + request.moduleId() + " nicht gefunden."));

        // 3. Modulangebot im Semester des Plans suchen, sonst 409
        ModuleOffering moduleOffering = moduleOfferingRepository.findByModuleIdAndSemesterId(module.getId(), semesterPlan.getSemester().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " wird in diesem Semester nicht angeboten."));

        // 4. Schon im Plan? Dann 409
        if (enrollmentRepository.existsBySemesterPlanIdAndModuleOfferingId(semesterPlan.getId(), moduleOffering.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " ist bereits im Plan.");
        }

        // 5. Belegung anlegen und speichern
        Enrollment enrollment = new Enrollment();
        enrollment.setSemesterPlan(semesterPlan);
        enrollment.setModuleOffering(moduleOffering);
        Enrollment saved = enrollmentRepository.save(enrollment);

        // 6. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    // VERSCHIEBEN einer Belegung vom aktuellen Semesterplan in einen anderen
    // Das Frontend schickt nur den Semesterplan, in den das Modul geschoben wird (Ziel)
    // das passende Modulangebot fuer dasselbe Modul im Zielsemester wird automatisch gesucht
    // (siehe enrollToSemesterPlan, Schritt 3).

    public EnrollmentResponseDTO moveEnrollment(Long semesterPlanId, Long enrollmentId, EnrollmentRequestMoveDTO request) {
        //1. Request - Body vorhanden, sonst 400 (bad request)
        if (request == null || request.targetSemesterPlanId() == null) {
            throw new InvalidRequestException("targetSemesterPlanId fehlt im Request-Body.");
        }

        // 2. Belegung suchen, aber nur wenn sie zu diesem Semesterplan gehoert, sonst 404 (not found)
        Enrollment enrollment = enrollmentRepository.findByIdAndSemesterPlanId(enrollmentId, semesterPlanId)
                        .orElseThrow(() -> new ResourceNotFoundException
                                ("Belegung " + enrollmentId + " in Semesterplan "
                                        + semesterPlanId + " nicht gefunden."));

        // 3. Ziel-Semesterplan suchen, sonst 400 (bad request)
        Long targetSemesterPlanId = request.targetSemesterPlanId();

        SemesterPlan targetPlan = semesterPlanRepository.findById(targetSemesterPlanId)
                .orElseThrow(() -> new InvalidRequestException("Der ausgewaehlte Semesterplan " + targetSemesterPlanId + " existiert nicht."));
        SemesterPlan sourcePlan = enrollment.getSemesterPlan();

        // 4.Kein sinnvolles Verschieben
        if (targetPlan.getId().equals(sourcePlan.getId()))
            throw new InvalidRequestException("Die Semesterplaene sind identisch.");

        // TODO: sobald die User-Zuordnung feststeht (siehe TODO in SemesterPlan),
        //  pruefen, ob targetPlan und sourcePlan zur selben Studentin gehoeren.

        // 5. Modulangebot im Zielsemester suchen, sonst 400 (bad request)
        StudyModule module = enrollment.getModuleOffering().getModule();
        ModuleOffering targetOffering = moduleOfferingRepository
                .findByModuleIdAndSemesterId(module.getId(), targetPlan.getSemester().getId())
                .orElseThrow(() -> new InvalidRequestException("Modul '" + module.getName() +
                        "' wird im ausgewaehltem Semester nicht angeboten."));

        // 6. Duplikat, dann 400
        if (enrollmentRepository.existsBySemesterPlanIdAndModuleOfferingId(targetPlan.getId(), targetOffering.getId())) {
            throw new InvalidRequestException("Modul ist im ausgewaehltem Semesterplan bereits enthalten.");
        }

        // 7. "Umhaengen"
        // Die bestehende Enrollment-Zeile in der Tabelle wird nicht gelöscht
        // und neu angelegt, sondern bekommt andere Fremdschlüssel von
        // Semesterplan (fk_semester_plan_id) und Modulangebot (fk_module_offering_id).
        enrollment.setSemesterPlan(targetPlan);
        enrollment.setModuleOffering(targetOffering);
        Enrollment saved = enrollmentRepository.save(enrollment);
        return toResponse(saved);

    }

    // Wandelt eine Belegung in das Response-DTO um (wird auch von SemesterPlanService genutzt)
    public static EnrollmentResponseDTO toResponse(Enrollment enrollment) {
        StudyModule module = enrollment.getModuleOffering().getModule();
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getSemesterPlan().getId(),
                enrollment.getModuleOffering().getId(),
                module.getId(),
                module.getName(),
                module.getEcts(),
                module.getWeeklyHours()
        );
    }

    // Belegung loeschen
    public void deleteEnrollment(Long semesterPlanId, Long enrollmentId)
    {
        Enrollment enrollment = enrollmentRepository.findByIdAndSemesterPlanId(enrollmentId, semesterPlanId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Belegung " + enrollmentId + " in Semesterplan " + semesterPlanId + " nicht gefunden."));
        enrollmentRepository.delete(enrollment);
    }
}
