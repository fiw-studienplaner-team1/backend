package htw.team1.studyPlanner.repository;

import htw.team1.studyPlanner.entity.*;
import htw.team1.studyPlanner.service.SemesterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BelegungRepositoryTest {


    @Autowired
    private BelegungRepository belegungRepository;
    @Autowired
    private ModulangebotRepository modulangebotRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SemesterService semesterService;

    @BeforeEach
    void setUp() {
        belegungRepository.deleteAll();
        modulangebotRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        belegungRepository.deleteAll();
        modulangebotRepository.deleteAll();
        moduleRepository.deleteAll();
    }

    @Test
    void findByPlanId_ShouldReturnOnlyEnrollmentsOfThisPlan() {
        // Arrange: Semester
        Semester semester = semesterService.getOrCreate(SemesterTyp.WISE, 2029);

        // Erstes Modul + Angebot
        StudyModule module = new StudyModule();
        module.setId("TEST1");
        module.setName("Testmodul");
        module.setSemester(1);
        module.setEcts(5);
        module.setSws(4);
        moduleRepository.save(module);

        Modulangebot offering = new Modulangebot();
        offering.setModule(module);
        offering.setSemester(semester);
        modulangebotRepository.save(offering);

        // Zweites Modul + Angebot
        StudyModule module2 = new StudyModule();
        module2.setId("TEST2");
        module2.setName("Testmodul 2");
        module2.setSemester(1);
        module2.setEcts(5);
        module2.setSws(4);
        moduleRepository.save(module2);

        Modulangebot offering2 = new Modulangebot();
        offering2.setModule(module2);
        offering2.setSemester(semester);
        modulangebotRepository.save(offering2);

        // Plan 1: zwei Belegungen
        Belegung b1 = new Belegung();
        b1.setPlanId(1L);
        b1.setOffering(offering);
        belegungRepository.save(b1);

        Belegung b2 = new Belegung();
        b2.setPlanId(1L);
        b2.setOffering(offering2);
        belegungRepository.save(b2);

        // Plan 2: eine Belegung (darf NICHT im Ergebnis auftauchen)
        Belegung b3 = new Belegung();
        b3.setPlanId(2L);
        b3.setOffering(offering);
        belegungRepository.save(b3);

        // Act
        List<Belegung> result = belegungRepository.findByPlanId(1L);

        // Assert
        assertThat(result).hasSize(2);

    }
}

