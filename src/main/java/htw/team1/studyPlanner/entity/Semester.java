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
    private ModulTurnus type;

    @Column(name ="jahr", nullable = false)
    private Integer year;

    @Column(name ="beginn")
    private LocalDate start;

    @Column(name= "ende")
    private LocalDate end;

    public Long getSemesterId() {
        return semesterId;
    }


    public ModulTurnus getType() {
        return type;
    }

    public Integer getYear() {
        return year;
    }

    public LocalDate getEnd() {
        return end;
    }

    public LocalDate getStart() {
        return start;
    }

    public Semester() {}
    // Ein Semester wird nur ueber diesen Konstruktor vollstaendig angelegt (Typ, Jahr, Beginn, Ende).
// Es gibt bewusst keine Setter: So kann kein unvollstaendiges Semester entstehen,
// und Typ/Jahr/Daten koennen nach dem Anlegen nicht mehr veraendert werden.
// Die ID vergibt die Datenbank. Der leere Konstruktor wird von JPA benoetigt.
    public Semester(ModulTurnus type, Integer year, LocalDate start, LocalDate end) {
        this.type = type;
        this.year = year;
        this.start = start;
        this.end = end;
    }





}