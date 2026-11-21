package htw.team1.studyPlanner.module.repository;

import htw.team1.studyPlanner.module.entity.ModuleOffering;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModuleOfferingRepository extends JpaRepository<ModuleOffering, Long> {

    // Das Angebot eines Moduls in einem bestimmten Semester (falls es eins gibt)
    Optional<ModuleOffering> findByModuleIdAndSemesterId(Long moduleId, Long semesterId);
}
