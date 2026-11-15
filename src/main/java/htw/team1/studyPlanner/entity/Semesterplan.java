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

}