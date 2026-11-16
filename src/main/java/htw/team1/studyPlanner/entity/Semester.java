package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "semester",
        uniqueConstraints = @UniqueConstraint(columnNames = {"typ", "jahr"})
)
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "typ", nullable = false)
    private SemesterTyp type;

    @Column(name ="jahr", nullable = false)
    private Integer year;

    @Column(name ="beginn")
    private LocalDate start;

    @Column(name= "ende")
    private LocalDate end;

    public Long getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Long semesterId) {
        this.semesterId = semesterId;
    }

    public SemesterTyp getType() {
        return type;
    }

    public void setType(SemesterTyp type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public Semester() {}






}