package htw.team1.studyPlanner.semesterplan.controller;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enrollment.entity.Enrollment;
import htw.team1.studyPlanner.module.entity.ModuleOffering;
import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.semesterplan.entity.SemesterPlan;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.enrollment.repository.EnrollmentRepository;
import htw.team1.studyPlanner.module.repository.ModuleOfferingRepository;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import htw.team1.studyPlanner.semesterplan.repository.SemesterPlanRepository;
import htw.team1.studyPlanner.semester.service.SemesterService;
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
class SemesterPlanControllerTest
{

    // Die feste Test-Studentin aus dem SemesterplanService
    private static final Long TEST_STUDENT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SemesterPlanRepository semesterplanRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ModuleOfferingRepository moduleOfferingRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SemesterService semesterService;

    private Semester winter;
    private Semester summer;

    @BeforeEach
    void setUp() {
        cleanUp();
        winter = semesterService.getOrCreate(Cycle.WISE, 2030);
        summer = semesterService.getOrCreate(Cycle.SOSE, 2030);
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    // Reihenfolge wichtig: erst was auf andere Tabellen zeigt, dann das, worauf gezeigt wird
    private void cleanUp() {
        enrollmentRepository.deleteAll();
        semesterplanRepository.deleteAll();
        moduleOfferingRepository.deleteAll();
        moduleRepository.deleteAll();
    }

    private SemesterPlan createPlan(Semester semester) {
        SemesterPlan plan = new SemesterPlan();
        plan.setUser(TEST_STUDENT_ID);
        plan.setSemester(semester);
        return semesterplanRepository.save(plan);
    }

    // ---------- Anlegen: POST /semesterplans ----------

    @Test
    void createPlan_WhenSemesterExists_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post("/semesterplans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterId\": " + summer.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.semesterId", is(summer.getId().intValue())))
                .andExpect(jsonPath("$.cycle", is("SOSE")))
                .andExpect(jsonPath("$.year", is(2030)));
    }

    @Test
    void createPlan_WhenPlanAlreadyExists_ShouldReturnConflict() throws Exception {
        createPlan(summer);

        mockMvc.perform(post("/semesterplans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"semesterId\": " + summer.getId() + "}"))
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
        // Arrange: Modul, angeboten im WiSe 2030
        StudyModule module = new StudyModule();
        module.setName("Testmodul");
        module.setRecommendedSemester(1);
        module.setEcts(5);
        module.setWeeklyHours(4);
        moduleRepository.save(module);

        ModuleOffering offering = new ModuleOffering();
        offering.setModule(module);
        offering.setSemester(winter);
        moduleOfferingRepository.save(offering);

        // WiSe-Plan zuerst anlegen (kleinere ID), damit wir sehen, dass nach Semester sortiert wird
        SemesterPlan winterPlan = createPlan(winter);
        createPlan(summer);

        Enrollment enrollment = new Enrollment();
        enrollment.setPlan(winterPlan);
        enrollment.setModuleOffering(offering);
        enrollmentRepository.save(enrollment);

        // Act + Assert
        mockMvc.perform(get("/semesterplans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                // SoSe 2030 beginnt im April, also vor WiSe 2030/31
                .andExpect(jsonPath("$[0].semesterLabel", is("SoSe 2030")))
                .andExpect(jsonPath("$[0].modules", hasSize(0)))
                .andExpect(jsonPath("$[1].semesterLabel", is("WiSe 2030/31")))
                .andExpect(jsonPath("$[1].modules", hasSize(1)))
                .andExpect(jsonPath("$[1].modules[0].name", is("Testmodul")))
                .andExpect(jsonPath("$[1].modules[0].ects", is(5)))
                .andExpect(jsonPath("$[1].modules[0].weeklyHours", is(4)));
    }
}
