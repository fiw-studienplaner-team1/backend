package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.Belegung;
import htw.team1.studyPlanner.entity.Modulangebot;
import htw.team1.studyPlanner.entity.Semesterplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BelegungRepository extends JpaRepository<Belegung,Long> {

    // Alle Belegungen eines Semesterplans
    List<Belegung> findByPlan(Semesterplan plan);

    // Ist dieses Modulangebot schon in diesem Plan eingeplant?
    boolean existsByPlanAndOffering(Semesterplan plan, Modulangebot offering);
}
