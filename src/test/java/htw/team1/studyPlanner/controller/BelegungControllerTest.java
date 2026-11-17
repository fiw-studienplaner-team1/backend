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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BelegungControllerTest {

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

    private Semesterplan winterPlan;
    private Modulangebot winterOffering;

    @BeforeEach
    void setUp() {
        cleanUp();

        Semester winter = semesterService.getOrCreate(ModulTurnus.WISE, 2030);
        Semester summer = semesterService.getOrCreate(ModulTurnus.SOSE, 2030);

        // TEST1 wird im WiSe angeboten, TEST2 nur im SoSe
        StudyModule module1 = saveModule("TEST1", "Testmodul Eins");
        StudyModule module2 = saveModule("TEST2", "Testmodul Zwei");
        winterOffering = saveOffering(module1, winter);
        saveOffering(module2, summer);

        // Plan der Test-Studentin im WiSe
        winterPlan = new Semesterplan();
        winterPlan.setUserId(1L);
        winterPlan.setSemester(winter);
        winterPlan = semesterplanRepository.save(winterPlan);
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        belegungRepository.deleteAll();
        semesterplanRepository.deleteAll();
        modulangebotRepository.deleteAll();
        moduleRepository.deleteAll();
    }

    private StudyModule saveModule(String id, String name) {
        StudyModule module = new StudyModule(id);
        module.setName(name);
        module.setSemester(1);
        module.setEcts(5);
        module.setSws(4);
        return moduleRepository.save(module);
    }

    private Modulangebot saveOffering(StudyModule module, Semester semester) {
        Modulangebot offering = new Modulangebot();
        offering.setModule(module);
        offering.setSemester(semester);
        return modulangebotRepository.save(offering);
    }

    private String url() {
        return "/semesterplans/" + winterPlan.getPlanId() + "/enrollments";
    }

    @Test
    void planModule_WhenOfferedAndNotPlanned_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": \"TEST1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enrollmentId", notNullValue()))
                .andExpect(jsonPath("$.planId", is(winterPlan.getPlanId().intValue())))
                .andExpect(jsonPath("$.moduleId", is("TEST1")))
                .andExpect(jsonPath("$.moduleName", is("Testmodul Eins")));
    }

    @Test
    void planModule_WhenAlreadyInPlan_ShouldReturnConflict() throws Exception {
        Belegung existing = new Belegung();
        existing.setPlan(winterPlan);
        existing.setOffering(winterOffering);
        belegungRepository.save(existing);

        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": \"TEST1\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void planModule_WhenNotOfferedInSemester_ShouldReturnConflict() throws Exception {
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": \"TEST2\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    void planModule_WhenModuleUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": \"GIBTSNICHT\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void planModule_WhenPlanUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/semesterplans/999999/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": \"TEST1\"}"))
                .andExpect(status().isNotFound());
    }
}