package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "semesterplan")
public class Semesterplan {

    // Primaerschluessel
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    // Gewuenschte ECTS
    @Column(name = "gewuenschte_ects")
    private Integer desiredEcts;

    //  Gewuenschte SWS
    @Column(name = "gewuenschte_sws")
    private Integer desiredSws;

    // TODO: Ersetzen durch @ManyToOne zu Benutzer, sobald die Entitaet existiert
    //       Dann auch Getter/Setter auf das Benutzer-Objekt umstellen
    @Column(name = "fk_benutzer_id")
    private Integer userId;

    // TODO: Ersetzen durch @ManyToOne zu Semester, sobald die Entitaet existiert
    //       Dann auch Getter/Setter auf das Semester-Objekt umstellen
    @Column(name ="fk_semester_id")
    private Integer semesterId;

    public Semesterplan() {}

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public Integer getDesiredEcts() {
        return desiredEcts;
    }

    public void setDesiredEcts(Integer desiredEcts) {
        this.desiredEcts = desiredEcts;
    }

    public Integer getDesiredSws() {
        return desiredSws;
    }

    public void setDesiredSws(Integer desiredSws) {
        this.desiredSws = desiredSws;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }





}