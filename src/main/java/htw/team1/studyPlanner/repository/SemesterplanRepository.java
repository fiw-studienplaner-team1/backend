package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Semesterplan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterplanRepository extends JpaRepository<Semesterplan, Long> {
}