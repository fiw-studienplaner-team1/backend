package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Belegung;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BelegungRepository extends JpaRepository<Belegung,Long> {

    List<Belegung> findByPlanId(Long planId);
}
