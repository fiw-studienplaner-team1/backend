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

    // vorlaeufig nur die ID, wird spaeter @ManyToOne zu Benutzer
    @Column(name = "fk_benutzer_id")
    private Integer userId;

    // vorlaeufig nur die ID, wird spaeter @ManyToOne zu Semester
    @Column(name ="fk_semester_id")
    private Integer semesterId;

    public Semesterplan() {}



}