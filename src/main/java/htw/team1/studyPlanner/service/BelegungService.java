package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.dto.BelegungRequestDTO;
import htw.team1.studyPlanner.dto.BelegungResponseDTO;
import htw.team1.studyPlanner.entity.*;
import htw.team1.studyPlanner.repository.BelegungRepository;
import htw.team1.studyPlanner.repository.ModulangebotRepository;
import htw.team1.studyPlanner.repository.ModuleRepository;
import htw.team1.studyPlanner.repository.SemesterplanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// TODO: Fehlerbehandlung im Team festlegen (ResponseStatusException im Service vs. eigene Exceptions
//       mit zentralem @ControllerAdvice) und dann hier und in den anderen Services einheitlich umsetzen
@Service
public class BelegungService {

    private final BelegungRepository belegungRepository;
    private final SemesterplanRepository semesterplanRepository;
    private final ModuleRepository moduleRepository;
    private final ModulangebotRepository modulangebotRepository;

    public BelegungService(BelegungRepository belegungRepository,
                           SemesterplanRepository semesterplanRepository,
                           ModuleRepository moduleRepository,
                           ModulangebotRepository modulangebotRepository) {
        this.belegungRepository = belegungRepository;
        this.semesterplanRepository = semesterplanRepository;
        this.moduleRepository = moduleRepository;
        this.modulangebotRepository = modulangebotRepository;
    }

    // Plant ein Modul in einen Semesterplan ein (legt eine Belegung an)
    public BelegungResponseDTO planModule(Long planId, BelegungRequestDTO request) {

        // 1. Plan suchen, sonst 404
        Semesterplan plan = semesterplanRepository.findById(planId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semesterplan " + planId + " nicht gefunden."));

        // 2. Modul suchen, sonst 404
        StudyModule module = moduleRepository.findById(request.moduleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Modul " + request.moduleId() + " nicht gefunden."));

        // 3. Modulangebot im Semester des Plans suchen, sonst 409
        Modulangebot offering = modulangebotRepository.findByModuleAndSemester(module, plan.getSemester()).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " wird in diesem Semester nicht angeboten."));

        // 4. Schon im Plan? Dann 409
        if (belegungRepository.existsByPlanAndOffering(plan, offering)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Modul " + module.getId() + " ist bereits im Plan.");
        }

        // 5. Belegung anlegen und speichern
        Belegung enrollment = new Belegung();
        enrollment.setPlan(plan);
        enrollment.setOffering(offering);
        Belegung saved = belegungRepository.save(enrollment);

        // 6. Als Response-DTO zurueckgeben
        return toResponse(saved);
    }

    private static BelegungResponseDTO toResponse(Belegung belegung) {
        return new BelegungResponseDTO(
                belegung.getEnrollmentId(),
                belegung.getPlan().getPlanId(),
                belegung.getOffering().getOfferingId(),
                belegung.getOffering().getModule().getId(),
                belegung.getOffering().getModule().getName()
        );
    }
}