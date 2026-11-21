package htw.team1.studyPlanner.enrollment.repository;

import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByPlanId(Long planId);

    // Ist dieses Modulangebot schon in diesem Plan eingeplant?
    boolean existsByPlanIdAndModuleOfferingId(Long planId, Long moduleOfferingId);

    // Alle Belegungen mehrerer Plaene auf einmal, inkl. Angebot und Modul (gegen N+1)
    @EntityGraph(attributePaths = {"plan", "moduleOffering", "moduleOffering.module"}) //"moduleOffering.semester" war hier ueberfluessig
    List<Enrollment> findByPlanIn(List<SemesterPlan> plans);
}
