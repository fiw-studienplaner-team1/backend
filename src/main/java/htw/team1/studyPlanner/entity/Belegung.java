package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "belegung",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_plan_id", "fk_modulangebot_id"})
)
public class Belegung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "belegung_id")
    private Long enrollmentId;

    // Note, z. B. 1.7 (wird spaeter befuellt)
    @Column(name = "note", precision = 2, scale = 1)
    private BigDecimal grade;

    @Column(name = "status")
    private String status;

    // TODO: Ersetzen durch @ManyToOne zu Semesterplan, sobald die Entitaet existiert
//       Dann auch Getter/Setter auf das Semesterplan-Objekt umstellen
    @Column(name = "fk_plan_id", nullable = false)
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "fk_modulangebot_id", nullable = false)
    private Modulangebot offering;

    public Belegung() {
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Modulangebot getOffering() {
        return offering;
    }

    public void setOffering(Modulangebot offering) {
        this.offering = offering;
    }


}