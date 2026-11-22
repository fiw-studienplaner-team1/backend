package htw.team1.studyPlanner.semesterplan.repository;

import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterPlanRepository extends JpaRepository<SemesterPlan, Long> {
    // Gibt es fuer diese Studentin in diesem Semester schon einen Plan?
    boolean existsByUserAndSemesterId(Long userId, Long semesterId);

    // Alle Plaene einer Studentin, sortiert nach Semesterbeginn (Semester gleich mitladen)
    @EntityGraph(attributePaths = "semester")
    List<SemesterPlan> findByUserOrderBySemesterStartDateAsc(Long userId);
}
