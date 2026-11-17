package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.dto.SemesterplanRequestDTO;
import htw.team1.studyPlanner.dto.SemesterplanResponseDTO;
import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.Semesterplan;
import htw.team1.studyPlanner.repository.SemesterRepository;
import htw.team1.studyPlanner.repository.SemesterplanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SemesterplanService {

    // TODO: Durch die eingeloggte Studentin ersetzen, sobald der Login fertig ist
    private static final Long TEST_STUDENT_ID = 1L;

    private final SemesterplanRepository semesterplanRepository;
    private final SemesterRepository semesterRepository;

    public SemesterplanService(SemesterplanRepository semesterplanRepository,
                               SemesterRepository semesterRepository) {
        this.semesterplanRepository = semesterplanRepository;
        this.semesterRepository = semesterRepository;
    }

    public SemesterplanResponseDTO createPlan(SemesterplanRequestDTO request) {
        // 1. Semester suchen, sonst 404
        Semester semester = semesterRepository.findById(request.semesterId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Semester " + request.semesterId() + " nicht gefunden."));

        // 2. Gibt es schon einen Plan in diesem Semester? Dann 409
        if(semesterplanRepository.existsByUserIdAndSemester(TEST_STUDENT_ID, semester)){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Fuer dieses Semester gibt es bereits einen Semesterplan.");
        }

        // 3. Neuen Plan anlegen und speichern
        Semesterplan plan = new Semesterplan();
        plan.setUserId(TEST_STUDENT_ID);
        plan.setSemester(semester);
        Semesterplan saved = semesterplanRepository.save(plan);

        // 4. Als Response-DTO zurueckgeben
        return toResponse(saved);
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

}