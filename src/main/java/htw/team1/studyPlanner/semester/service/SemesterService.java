package htw.team1.studyPlanner.semester.service;

import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.semester.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }
    // Liefert das Semester; legt es an, falls es noch nicht existiert
    public Semester getOrCreate(Cycle cycle, Integer year) {
       if(cycle == Cycle.SONST){
           throw new IllegalArgumentException("Ein Semester muss WISE oder SOSE sein.");
       }
        return semesterRepository.findByCycleAndYear(cycle, year)
                .orElseGet(() -> createSemester(cycle, year));
    }

    // Baut ein neues Semester mit berechneten Daten und speichert es
    private Semester createSemester(Cycle cycle, Integer year) {
        LocalDate startDate;
        LocalDate endDate;

        if (cycle == Cycle.SOSE) {
            startDate = LocalDate.of(year, 4, 1);
            endDate = LocalDate.of(year, 9, 30);
        } else {
            startDate = LocalDate.of(year, 10, 1);
            endDate = LocalDate.of(year + 1, 3, 31);
        }

        Semester semester = new Semester(cycle, year, startDate, endDate);
        return semesterRepository.save(semester);
    }
}
