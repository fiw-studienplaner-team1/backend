package htw.team1.studyPlanner.module.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import htw.team1.studyPlanner.module.entity.StudyModule;

public interface ModuleRepository extends JpaRepository<StudyModule, Long>
{

}
