package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findAllByOrderByStartAsc();
}