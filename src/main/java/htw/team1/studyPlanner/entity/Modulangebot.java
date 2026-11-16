package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "modulangebot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_modul_id", "fk_semester_id"})
)
public class Modulangebot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modulangebot_id")
    private Long offeringId;

    @Column(name = "pruefungsform")
    private String examType;

    @ManyToOne
    @JoinColumn(name ="fk_modul_id", nullable = false)
    private StudyModule module;

    // Verknuepfung zum Semester
    @ManyToOne
    @JoinColumn(name = "fk_semester_id", nullable = false)
    private Semester semester;

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Long getOfferingId() {
        return offeringId;
    }

    public void setOfferingId(Long offeringId) {
        this.offeringId = offeringId;
    }

    public StudyModule getModule() {
        return module;
    }

    public void setModule(StudyModule module) {
        this.module = module;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Modulangebot() {}

}