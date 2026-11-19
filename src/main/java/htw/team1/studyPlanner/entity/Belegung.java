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

    // Jede Belegung gehoert zu genau einem Semesterplan (viele Belegungen -> ein Plan)
    @ManyToOne
    @JoinColumn(name = "fk_plan_id", nullable = false)
    private Semesterplan plan;

    @ManyToOne
    @JoinColumn(name = "fk_modulangebot_id", nullable = false)
    private Modulangebot offering;

    public Belegung() {
    }

    public Long getEnrollmentId() {
        return enrollmentId;
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

    public Semesterplan getPlan() {
        return plan;
    }

    public void setPlan(Semesterplan plan) {
        this.plan = plan;
    }

    public Modulangebot getOffering() {
        return offering;
    }

    public void setOffering(Modulangebot offering) {
        this.offering = offering;
    }



}