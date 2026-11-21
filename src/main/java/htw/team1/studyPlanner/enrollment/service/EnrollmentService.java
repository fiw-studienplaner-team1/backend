package htw.team1.studyPlanner.enrollment.service;

import htw.team1.studyPlanner.enrollment.dto.EnrollmentRequestDTO;
import htw.team1.studyPlanner.enrollment.dto.EnrollmentResponseDTO;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.enrollment.repository.EnrollmentRepository;
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
//       mit zentralem @ControllerAdvice) und dann hier und in den anderen Services einheitlich umsetzen
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

    // Plant ein Modul in einen Semesterplan ein (legt eine Belegung an)
    public EnrollmentResponseDTO planModule(Long planId, EnrollmentRequestDTO request) {

        // 1. Plan suchen, sonst 404
        SemesterPlan plan = semesterPlanRepository.findById(planId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semesterplan " + planId + " nicht gefunden."));

        // 2. Modul suchen, sonst 404
        StudyModule module = moduleRepository.findById(request.moduleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Modul " + request.moduleId() + " nicht gefunden."));

        // 3. Modulangebot im Semester des Plans suchen, sonst 409
        ModuleOffering offering = moduleOfferingRepository.findByModuleIdAndSemesterId(module.getId(), plan.getSemester().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " wird in diesem Semester nicht angeboten."));

        // 4. Schon im Plan? Dann 409
        if (enrollmentRepository.existsByPlanIdAndModuleOfferingId(plan.getId(), offering.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " ist bereits im Plan.");
        }

        // 5. Belegung anlegen und speichern
        Enrollment enrollment = new Enrollment();
        enrollment.setPlan(plan);
        enrollment.setModuleOffering(offering);
        Enrollment saved = enrollmentRepository.save(enrollment);

        // 6. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    private static EnrollmentResponseDTO toResponse(Enrollment enrollment) {
        return new EnrollmentResponseDTO(
                enrollment.getId(),
                enrollment.getPlan().getId(),
                enrollment.getModuleOffering().getId(),
                enrollment.getModuleOffering().getModule().getId(),
                enrollment.getModuleOffering().getModule().getName()
        );
    }
}
