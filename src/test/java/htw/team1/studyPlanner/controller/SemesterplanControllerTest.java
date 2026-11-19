package htw.team1.studyPlanner.controller;

import htw.team1.studyPlanner.entity.*;
import htw.team1.studyPlanner.repository.BelegungRepository;
import htw.team1.studyPlanner.repository.ModulangebotRepository;
import htw.team1.studyPlanner.repository.ModuleRepository;
import htw.team1.studyPlanner.repository.SemesterplanRepository;
import htw.team1.studyPlanner.service.SemesterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SemesterplanControllerTest {

    // Die feste Test-Studentin aus dem SemesterplanService
    private static final Long TEST_STUDENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SemesterplanRepository semesterplanRepository;
    @Autowired
    private BelegungRepository belegungRepository;
    @Autowired
    private ModulangebotRepository modulangebotRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SemesterService semesterService;

    private Semester winter;
    private Semester summer;

    @BeforeEach
    void setUp() {
        cleanUp();
        winter = semesterService.getOrCreate(ModulTurnus.WISE, 2030);
        summer = semesterService.getOrCreate(ModulTurnus.SOSE, 2030);
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    // Reihenfolge wichtig: erst was auf andere Tabellen zeigt, dann das, worauf gezeigt wird
    private void cleanUp() {
        belegungRepository.deleteAll();
        semesterplanRepository.deleteAll();
        modulangebotRepository.deleteAll();
        moduleRepository.deleteAll();
    }

    private Semesterplan createPlan(Semester semester) {
        Semesterplan plan = new Semesterplan();
        plan.setUserId(TEST_STUDENT_ID);
        plan.setSemester(semester);
        return semesterplanRepository.save(plan);
    }

    // ---------- Anlegen: POST /semesterplans ----------

    @Test
    void createPlan_WhenSemesterExists_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/semesterplans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterId\": " + summer.getSemesterId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.planId", notNullValue()))
                .andExpect(jsonPath("$.semesterId", is(summer.getSemesterId().intValue())))
                .andExpect(jsonPath("$.type", is("SOSE")))
                .andExpect(jsonPath("$.year", is(2030)));
    }

    @Test
    void createPlan_WhenPlanAlreadyExists_ShouldReturnConflict() throws Exception {
        createPlan(summer);

        mockMvc.perform(post("/semesterplans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterId\": " + summer.getSemesterId() + "}"))
                .andExpect(status().isConflict());
    }

    @Test
    void createPlan_WhenSemesterUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/semesterplans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterId\": 999999}"))
                .andExpect(status().isNotFound());
    }

    // ---------- Laden: GET /semesterplans ----------

    @Test
    void getPlans_WhenNoPlans_ShouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/semesterplans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getPlans_WhenPlansExist_ShouldReturnSortedPlansWithModules() throws Exception {
        // Arrange: Modul TEST1, angeboten im WiSe 2030
        StudyModule module = new StudyModule("TEST1");
        module.setName("Testmodul");
        module.setSemester(1);
        module.setEcts(5);
        module.setSws(4);
        moduleRepository.save(module);

        Modulangebot offering = new Modulangebot();
        offering.setModule(module);
        offering.setSemester(winter);
        modulangebotRepository.save(offering);

        // WiSe-Plan zuerst anlegen (kleinere ID), damit wir sehen, dass nach Semester sortiert wird
        Semesterplan winterPlan = createPlan(winter);
        createPlan(summer);

        Belegung enrollment = new Belegung();
        enrollment.setPlan(winterPlan);
        enrollment.setOffering(offering);
        belegungRepository.save(enrollment);

        // Act + Assert
        mockMvc.perform(get("/semesterplans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                // SoSe 2030 beginnt im April, also vor WiSe 2030/31
                .andExpect(jsonPath("$[0].semesterLabel", is("SoSe 2030")))
                .andExpect(jsonPath("$[0].modules", hasSize(0)))
                .andExpect(jsonPath("$[1].semesterLabel", is("WiSe 2030/31")))
                .andExpect(jsonPath("$[1].modules", hasSize(1)))
                .andExpect(jsonPath("$[1].modules[0].moduleId", is("TEST1")))
                .andExpect(jsonPath("$[1].modules[0].name", is("Testmodul")))
                .andExpect(jsonPath("$[1].modules[0].ects", is(5)))
                .andExpect(jsonPath("$[1].modules[0].sws", is(4)));
    }
}