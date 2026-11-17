package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Belegung;
import htw.team1.studyPlanner.entity.Modulangebot;
import htw.team1.studyPlanner.entity.SemesterTyp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BelegungRepository extends JpaRepository<Belegung,Long> {

    List<Belegung> findByPlanId(Long planId);
}
