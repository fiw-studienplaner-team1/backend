package htw.team1.studyPlanner.enrollment.repository;

import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findBySemesterPlanId(Long semesterPlanId);

    // Ist dieses Modulangebot schon in diesem Plan eingeplant?
    boolean existsBySemesterPlanIdAndModuleOfferingId(Long semesterPlanId, Long moduleOfferingId);

    // Alle Belegungen mehrerer Plaene auf einmal, inkl. Angebot und Modul (gegen N+1)
    @EntityGraph(attributePaths = {"semesterPlan", "moduleOffering", "moduleOffering.module"})
    List<Enrollment> findBySemesterPlanIn(List<SemesterPlan> semesterPlans); // Ist hier ein Typo?

    // Finde die Belegung mit dieser Id (=EnrollmentId) aber nur, wenn sie zu diesem Semesterplan gehört.
    //Passt nicht, dann leer und 404 (not found) im Service
    Optional<Enrollment> findByIdAndSemesterPlanId(Long Id, Long semesterPlanId);
}
