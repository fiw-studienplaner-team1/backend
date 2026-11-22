package htw.team1.studyPlanner.enrollment.entity;

import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_semester_plan_id", "fk_module_offering_id"})
)
public class Enrollment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Jede Belegung gehoert zu genau einem Semesterplan (viele Belegungen -> ein Plan)
    @ManyToOne
    @JoinColumn(name = "fk_semester_plan_id", nullable = false)
    private SemesterPlan semesterPlan;

    @ManyToOne
    @JoinColumn(name = "fk_module_offering_id", nullable = false)
    private ModuleOffering moduleOffering;

    public Enrollment() {}

    public Long getId() {
        return id;
    }

    public SemesterPlan getSemesterPlan()
    {
        return semesterPlan;
    }

    public void setSemesterPlan(SemesterPlan semesterPlan)
    {
        this.semesterPlan = semesterPlan;
    }

    public ModuleOffering getModuleOffering()
    {
        return moduleOffering;
    }

    public void setModuleOffering(ModuleOffering moduleOffering)
    {
        this.moduleOffering = moduleOffering;
    }
}
