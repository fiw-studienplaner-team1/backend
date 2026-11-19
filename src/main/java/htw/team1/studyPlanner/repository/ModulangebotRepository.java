package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Modulangebot;
import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.StudyModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModulangebotRepository extends JpaRepository<Modulangebot, Long> {

    // Das Angebot eines Moduls in einem bestimmten Semester (falls es eins gibt)
    Optional<Modulangebot> findByModuleAndSemester(StudyModule module, Semester semester);
}