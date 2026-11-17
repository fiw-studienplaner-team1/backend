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
        return semesterRepository.findByTypeAndYear(type, year)
                .orElseGet(() -> createSemester(type, year));
    }

    // Baut ein neues Semester mit berechneten Daten und speichert es
    private Semester createSemester(ModulTurnus type, Integer year) {
        Semester semester = new Semester();
        semester.setType(type);
        semester.setYear(year);

        if (type == ModulTurnus.SOSE) {
            semester.setStart(LocalDate.of(year, 4, 1));
            semester.setEnd(LocalDate.of(year, 9, 30));
        } else {
            semester.setStart(LocalDate.of(year, 10, 1));
            semester.setEnd(LocalDate.of(year + 1, 3, 31));
        }


        return semesterRepository.save(semester);
    }
}