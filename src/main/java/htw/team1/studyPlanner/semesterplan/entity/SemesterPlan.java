package htw.team1.studyPlanner.semesterplan.entity;

import htw.team1.studyPlanner.semester.entity.Semester;
import jakarta.persistence.*;

@Entity
@Table(name = "semester_plans", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_user_id", "fk_semester_id"}))
public class SemesterPlan
{

    // Primaerschluessel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Gewuenschte ECTS
    @Column(name = "target_ects")
    private Integer targetEcts;

    //  Gewuenschte SWS
    @Column(name = "target_weekly_hours")
    private Integer targetWeeklyHours;

    // TODO: Ersetzen durch @ManyToOne zu Benutzer, sobald die Entitaet existiert
    //       Dann auch Getter/Setter auf das Benutzer-Objekt umstellen

    @Column(name = "fk_user_id")
    private Long user;

    // Jeder Semesterplan gehoert zu genau einem Semester (viele Plaene -> ein Semester)
    @ManyToOne
    @JoinColumn(name ="fk_semester_id", nullable = false)
    private Semester semester;

    public SemesterPlan() {}

    public Long getId() {
        return id;
    }


    public Integer getTargetEcts()
    {
        return targetEcts;
    }

    public void setTargetEcts(Integer targetEcts)
    {
        this.targetEcts = targetEcts;
    }

    public Integer getTargetWeeklyHours()
    {
        return targetWeeklyHours;
    }

    public void setTargetWeeklyHours(Integer targetWeeklyHours)
    {
        this.targetWeeklyHours = targetWeeklyHours;
    }

    public Long getUser()
    {
        return user;
    }

    public void setUser(Long user)
    {
        this.user = user;
    }

    public Semester getSemester()
    {
        return semester;
    }

    public void setSemester(Semester semester)
    {
        this.semester = semester;
    }
}
