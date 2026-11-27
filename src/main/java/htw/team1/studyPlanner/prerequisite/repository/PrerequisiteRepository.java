package htw.team1.studyPlanner.prerequisite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import htw.team1.studyPlanner.prerequisite.entity.Prerequisite;

import java.util.List;
import java.util.Optional;


//TODO erweitern um nicht-Modul-Vorraussetzung-abfragen
public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Integer>
{
	@Query("SELECT m FROM Prerequisite m WHERE m.successorModule.moduleNumber = :moduleNumber")
	Optional<List<Prerequisite>> findBySuccessorModuleNumber(String moduleNumber);
	
	@Query("SELECT m FROM Prerequisite m WHERE m.predecessorModule.moduleNumber = :moduleNumber")
	Optional<List<Prerequisite>> findByPredecessorModuleId(String moduleNumber);	
}
