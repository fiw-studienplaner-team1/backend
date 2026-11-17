package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.Semesterplan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterplanRepository extends JpaRepository<Semesterplan, Long> {
    // Gibt es fuer diese Studentin in diesem Semester schon einen Plan?
    boolean existsByUserIdAndSemester(Long userId, Semester semester);

    // Alle Plaene einer Studentin, sortiert nach Semesterbeginn (Semester gleich mitladen)
    @EntityGraph(attributePaths = "semester")
    List<Semesterplan> findByUserIdOrderBySemesterStartAsc(Long userId);
}