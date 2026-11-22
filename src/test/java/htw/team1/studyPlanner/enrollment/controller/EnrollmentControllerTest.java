package htw.team1.studyPlanner.enrollment.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest
{

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SemesterPlanRepository semesterPlanRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private ModuleOfferingRepository moduleOfferingRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private SemesterService semesterService;

    private SemesterPlan winterPlan;
    private ModuleOffering winterOffering;

    @BeforeEach
    void setUp() {
        cleanUp();

        Semester winter = semesterService.getOrCreate(Cycle.WISE, 2030);
        Semester summer = semesterService.getOrCreate(Cycle.SOSE, 2030);

        // Module werden im WiSe bzw. SoSe angeboten
        StudyModule module1 = saveModule("Testmodul Eins");
        StudyModule module2 = saveModule("Testmodul Zwei");
        winterOffering = saveOffering(module1, winter);
        saveOffering(module2, summer);

        // Plan der Test-Studentin im WiSe
        winterPlan = new SemesterPlan();
        winterPlan.setUser(1L);
        winterPlan.setSemester(winter);
        winterPlan = semesterPlanRepository.save(winterPlan);
    }

    @AfterEach
    void tearDown() {
        cleanUp();
    }

    private void cleanUp() {
        enrollmentRepository.deleteAll();
        semesterPlanRepository.deleteAll();
        moduleOfferingRepository.deleteAll();
        moduleRepository.deleteAll();
    }

    private StudyModule saveModule(String name) {
        StudyModule module = new StudyModule();
        module.setName(name);
        module.setRecommendedSemester(1);
        module.setEcts(5);
        module.setWeeklyHours(4);
        return moduleRepository.save(module);
    }

    private ModuleOffering saveOffering(StudyModule module, Semester semester) {
        ModuleOffering offering = new ModuleOffering();
        offering.setModule(module);
        offering.setSemester(semester);
        return moduleOfferingRepository.save(offering);
    }

    private String url() {
        return "/semesterplans/" + winterPlan.getId() + "/enrollments";
    }

    @Test
    void enrollToSemesterPlan_WhenOfferedAndNotPlanned_ShouldReturnCreated() throws Exception {
        StudyModule module1 = moduleRepository.findAll().get(0);
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": " + module1.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.semesterPlanId", is(winterPlan.getId().intValue())))
                .andExpect(jsonPath("$.moduleId", is(module1.getId().intValue())))
                .andExpect(jsonPath("$.moduleName", is("Testmodul Eins")));
    }

    @Test
    void enrollToSemesterPlan_WhenAlreadyInPlan_ShouldReturnConflict() throws Exception {
        Enrollment existing = new Enrollment();
        existing.setSemesterPlan(winterPlan);
        existing.setModuleOffering(winterOffering);
        enrollmentRepository.save(existing);

        StudyModule module1 = moduleRepository.findAll().get(0);
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": " + module1.getId() + "}"))
                .andExpect(status().isConflict());
    }

    @Test
    void enrollToSemesterPlan_WhenNotOfferedInSemester_ShouldReturnConflict() throws Exception {
        StudyModule module2 = moduleRepository.findAll().get(1);
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": " + module2.getId() + "}"))
                .andExpect(status().isConflict());
    }

    @Test
    void enrollToSemesterPlan_WhenModuleUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": 999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void enrollToSemesterPlan_WhenPlanUnknown_ShouldReturnNotFound() throws Exception {
        StudyModule module1 = moduleRepository.findAll().get(0);
        mockMvc.perform(post("/semesterplans/999999/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": " + module1.getId() + "}"))
                .andExpect(status().isNotFound());
    }
}
