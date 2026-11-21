package htw.team1.studyPlanner.semester.repository;

import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.enums.Cycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    // TODO: Wird im Moment noch nirgends aufgerufen. Fuer spaeter gedacht, z. B. wenn ein
    //       Endpunkt alle Semester fuer die Semesterkacheln im Modulplaner liefern soll,
    //       in der richtigen Reihenfolge.
    List<Semester> findAllByOrderByStartDateAsc();

    Optional<Semester> findByCycleAndYear(Cycle cycle, Integer year);
}
