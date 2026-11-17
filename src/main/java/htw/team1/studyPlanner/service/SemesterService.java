package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.ModulTurnus;
import htw.team1.studyPlanner.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }
    // Liefert das Semester; legt es an, falls es noch nicht existiert
    // TODO: ModulTurnus wird noch umbenannt (nach Donnerstag)
    public Semester getOrCreate(ModulTurnus type, Integer year) {
       if(type == ModulTurnus.SONST){
           throw new IllegalArgumentException("Ein Semester muss WISE oder SOSE sein.");
       }
        return semesterRepository.findByTypeAndYear(type, year)
                .orElseGet(() -> createSemester(type, year));
    }

    // Baut ein neues Semester mit berechneten Daten und speichert es
    private Semester createSemester(ModulTurnus type, Integer year) {
        LocalDate start;
        LocalDate end;

        if (type == ModulTurnus.SOSE) {
            start = LocalDate.of(year, 4, 1);
            end = LocalDate.of(year, 9, 30);
        } else {
            start = LocalDate.of(year, 10, 1);
            end = LocalDate.of(year + 1, 3, 31);
        }

        Semester semester = new Semester(type, year, start, end);
        return semesterRepository.save(semester);
    }
}