package htw.team1.studyPlanner.enrollment.repository;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.semester.service.SemesterService;
import htw.team1.studyPlanner.module.repository.ModuleOfferingRepository;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import htw.team1.studyPlanner.semesterplan.repository.SemesterPlanRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EnrollmentRepositoryTest
{


    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ModuleOfferingRepository moduleOfferingRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SemesterService semesterService;
    @Autowired
    private SemesterPlanRepository semesterplanRepository;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        semesterplanRepository.deleteAll();
        moduleOfferingRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        enrollmentRepository.deleteAll();
        semesterplanRepository.deleteAll();
        moduleOfferingRepository.deleteAll();
        moduleRepository.deleteAll();
    }



    @Test
    void findByPlanId_ShouldReturnOnlyEnrollmentsOfThisPlan() {
        // Arrange: Semester
        Semester semester = semesterService.getOrCreate(Cycle.WISE, 2029);

        // Erstes Modul + Angebot
        StudyModule module = new StudyModule();
        module.setName("Testmodul");
        module.setRecommendedSemester(1);
        module.setEcts(5);
        module.setWeeklyHours(4);
        moduleRepository.save(module);

        ModuleOffering offering = new ModuleOffering();
        offering.setModule(module);
        offering.setSemester(semester);
        moduleOfferingRepository.save(offering);

        // Zweites Modul + Angebot
        StudyModule module2 = new StudyModule();
        module2.setName("Testmodul 2");
        module2.setRecommendedSemester(1);
        module2.setEcts(5);
        module2.setWeeklyHours(4);
        moduleRepository.save(module2);

        ModuleOffering offering2 = new ModuleOffering();
        offering2.setModule(module2);
        offering2.setSemester(semester);
        moduleOfferingRepository.save(offering2);

        // Zwei Semesterplaene (verschiedene Studentinnen, wegen UniqueConstraint)
        SemesterPlan plan1 = new SemesterPlan();
        plan1.setUser(1L);
        plan1.setSemester(semester);
        semesterplanRepository.save(plan1);

        SemesterPlan plan2 = new SemesterPlan();
        plan2.setUser(2L);
        plan2.setSemester(semester);
        semesterplanRepository.save(plan2);


        // Plan 1: zwei Belegungen
        Enrollment b1 = new Enrollment();
        b1.setPlan(plan1);
        b1.setModuleOffering(offering);
        enrollmentRepository.save(b1);

        Enrollment b2 = new Enrollment();
        b2.setPlan(plan1);
        b2.setModuleOffering(offering2);
        enrollmentRepository.save(b2);

        // Plan 2: eine Belegung (darf NICHT im Ergebnis auftauchen)
        Enrollment b3 = new Enrollment();
        b3.setPlan(plan2);
        b3.setModuleOffering(offering);
        enrollmentRepository.save(b3);

        // Act
        List<Enrollment> result = enrollmentRepository.findByPlanId(plan1.getId());

        // Assert
        assertThat(result).hasSize(2);


    }
}
