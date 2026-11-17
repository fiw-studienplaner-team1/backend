package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.Semesterplan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterplanRepository extends JpaRepository<Semesterplan, Long> {
    // Gibt es fuer diese Studentin in diesem Semester schon einen Plan?
    boolean existsByUserIdAndSemester(Long userId, Semester semester);
}