package htw.team1.studyPlanner.semester.entity;

import htw.team1.studyPlanner.enums.Cycle;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(
        name = "semesters",
        uniqueConstraints = @UniqueConstraint(columnNames = {"cycle", "year"})
)
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle", nullable = false)
    private Cycle cycle;

    @Column(name ="year", nullable = false)
    private Integer year;

    @Column(name ="start_date")
    private LocalDate startDate;

    @Column(name= "end_date")
    private LocalDate endDate;

    public Long getId() {
        return id;
    }

    public Cycle getCycle() {
        return cycle;
    }

    public Integer getYear() {
        return year;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public Semester() {}
    // Ein Semester wird nur ueber diesen Konstruktor vollstaendig angelegt (Typ, Jahr, Beginn, Ende).
    // Es gibt bewusst keine Setter: So kann kein unvollstaendiges Semester entstehen,
    // und Typ/Jahr/Daten koennen nach dem Anlegen nicht mehr veraendert werden.
    // Die ID vergibt die Datenbank. Der leere Konstruktor wird von JPA benoetigt.
    public Semester(Cycle cycle, Integer year, LocalDate startDate, LocalDate endDate) {
        this.cycle = cycle;
        this.year = year;
        this.startDate = startDate;
        this.endDate = endDate;
    }



}
