package htw.team1.studyPlanner.module.entity;

import htw.team1.studyPlanner.semester.entity.Semester;
import jakarta.persistence.*;

@Entity
@Table(
        name = "module_offerings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"fk_module_id", "fk_semester_id"})
)
public class ModuleOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "exam_type")
    private String examType;

    @ManyToOne
    @JoinColumn(name ="fk_module_id", nullable = false)
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

    public Long getId() {
        return id;
    }

    public StudyModule getModule() {
        return module;
    }

    public void setModule(StudyModule module){this.module = module;}

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public ModuleOffering() {}

}
