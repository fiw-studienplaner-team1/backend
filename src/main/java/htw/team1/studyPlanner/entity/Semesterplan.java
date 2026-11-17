package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "semesterplan", uniqueConstraints = @UniqueConstraint(columnNames = {"fk_benutzer_id", "fk_semester_id"}))
public class Semesterplan {

    // Primaerschluessel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    // TODO: desiredEcts und desiredSws in Semesterplan sind Long, bei StudyModule sind ECTS und SWS Integer.
    //       Im Schema sind alle integer. Das ist unkritisch, aber uneinheitlich (bei der Glossar-Anpassung vereinheitlichen).
    // Gewuenschte ECTS
    @Column(name = "gewuenschte_ects")
    private Long desiredEcts;

    //  Gewuenschte SWS
    @Column(name = "gewuenschte_sws")
    private Long desiredSws;

    // TODO: Ersetzen durch @ManyToOne zu Benutzer, sobald die Entitaet existiert
    //       Dann auch Getter/Setter auf das Benutzer-Objekt umstellen

    @Column(name = "fk_benutzer_id")
    private Long userId;

    // Jeder Semesterplan gehoert zu genau einem Semester (viele Plaene -> ein Semester)
    @ManyToOne
    @JoinColumn(name ="fk_semester_id", nullable = false)
    private Semester semester;

    public Semesterplan() {}

    public Long getPlanId() {
        return planId;
    }

    public Long getDesiredEcts() {
        return desiredEcts;
    }

    public void setDesiredEcts(Long desiredEcts) {
        this.desiredEcts = desiredEcts;
    }

    public Long getDesiredSws() {
        return desiredSws;
    }

    public void setDesiredSws(Long desiredSws) {
        this.desiredSws = desiredSws;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }





}