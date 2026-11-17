package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "semesterplan")
public class Semesterplan {

    // Primaerschluessel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

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

    // TODO: Ersetzen durch @ManyToOne zu Semester, sobald die Entitaet existiert
    //       Dann auch Getter/Setter auf das Semester-Objekt umstellen
    @Column(name ="fk_semester_id")
    private Long semesterId;

    public Semesterplan() {}

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
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

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }





}