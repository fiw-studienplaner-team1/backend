package htw.team1.studyPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import htw.team1.studyPlanner.entity.StudyModule;

public interface ModuleRepository extends JpaRepository<StudyModule, String>
{

}
