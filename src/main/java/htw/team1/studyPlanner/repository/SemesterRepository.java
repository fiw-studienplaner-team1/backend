package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.SemesterTyp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    // TODO: Wird im Moment noch nirgends aufgerufen. Fuer spaeter gedacht, z. B. wenn ein
//       Endpunkt alle Semester fuer die Semesterkacheln im Modulplaner liefern soll,
//       in der richtigen Reihenfolge.
    List<Semester> findAllByOrderByStartAsc();

    Optional<Semester> findByTypeAndYear(SemesterTyp type, Integer year);
}