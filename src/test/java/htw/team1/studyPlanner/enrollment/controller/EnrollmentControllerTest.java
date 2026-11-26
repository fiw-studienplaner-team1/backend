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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerTest {

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

    //Neu: wird von mehreren Tests verwendet
    private StudyModule movableModule;

    private SemesterPlan winterPlan;
    private ModuleOffering winterOffering;

    private SemesterPlan summerPlan;
    private ModuleOffering summerOffering;

    private SemesterPlan planWithoutModuleOfferings;

    @BeforeEach
    void setUp() {
        cleanUp();

        Semester winter = semesterService.getOrCreate(Cycle.WISE, 2030);
        Semester summer = semesterService.getOrCreate(Cycle.SOSE, 2030);
        Semester emptySemester = semesterService.getOrCreate(Cycle.WISE, 2031);


        // Module werden im WiSe bzw. SoSe angeboten
        movableModule = saveModule("Testmodul 1");
        StudyModule otherModule = saveModule("Testmodul 2");
        winterOffering = saveOffering(movableModule, winter);
        saveOffering(otherModule, summer);
        summerOffering = saveOffering(movableModule, summer);

        // Plan der Test-Studentin im WiSe
        winterPlan = new SemesterPlan();
        winterPlan.setUser(1L);
        winterPlan.setSemester(winter);
        winterPlan = semesterPlanRepository.save(winterPlan);

        summerPlan = new SemesterPlan();
        summerPlan.setUser(1L);
        summerPlan.setSemester(summer);
        summerPlan = semesterPlanRepository.save(summerPlan);

        planWithoutModuleOfferings = new SemesterPlan();
        planWithoutModuleOfferings.setUser(1L);
        planWithoutModuleOfferings.setSemester(emptySemester);
        planWithoutModuleOfferings = semesterPlanRepository.save(planWithoutModuleOfferings);
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

    private Enrollment createEnrollment(SemesterPlan plan, ModuleOffering offering) {
        Enrollment enrollment = new Enrollment();
        enrollment.setSemesterPlan(plan);
        enrollment.setModuleOffering(offering);
        return enrollmentRepository.save(enrollment);
    }

    private String url() {
        return "/semesterplans/" + winterPlan.getId() + "/enrollments";
    }

    private String moveUrl(Long semesterPlanId, Long enrollmentId) {
        return "/semesterplans/" + semesterPlanId + "/enrollments/" + enrollmentId + "/move";
    }

    private String deleteUrl(Long semesterPlanId, Long enrollmentId) {
        return "/semesterplans/" + semesterPlanId + "/enrollments/" + enrollmentId;
    }

    @Test
    void enrollToSemesterPlan_WhenOfferedAndNotPlanned_ShouldReturnCreated() throws Exception {
        mockMvc.perform(post(url())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"moduleId\": " + movableModule.getId() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.semesterPlanId", is(winterPlan.getId().intValue())))
                .andExpect(jsonPath("$.moduleId", is(movableModule.getId().intValue())))
                .andExpect(jsonPath("$.moduleName", is(movableModule.getName())))
                .andExpect(jsonPath("$.ects", is(5)))
                .andExpect(jsonPath("$.weeklyHours", is(4)));
    }

    @Test
    void enrollToSemesterPlan_WhenAlreadyInPlan_ShouldReturnConflict() throws Exception {
        createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(post(url()).contentType(MediaType.APPLICATION_JSON).content("{\"moduleId\": " + movableModule.getId() + "}")).andExpect(status().isConflict());
    }

    @Test
    void enrollToSemesterPlan_WhenNotOfferedInSemester_ShouldReturnConflict() throws Exception {
        StudyModule module2 = moduleRepository.findAll().stream().filter(m -> !m.getId().equals(movableModule.getId())).findFirst().orElseThrow();
        mockMvc.perform(post(url()).contentType(MediaType.APPLICATION_JSON).content("{\"moduleId\": " + module2.getId() + "}")).andExpect(status().isConflict());
    }

    @Test
    void enrollToSemesterPlan_WhenModuleUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post(url()).contentType(MediaType.APPLICATION_JSON).content("{\"moduleId\": 999}")).andExpect(status().isNotFound());
    }

    @Test
    void enrollToSemesterPlan_WhenPlanUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/semesterplans/999999/enrollments").contentType(MediaType.APPLICATION_JSON).content("{\"moduleId\": " + movableModule.getId() + "}")).andExpect(status().isNotFound());
    }

    @Test
    void moveEnrollment_WhenTargetOffersModule_ShouldReturnOkAndMoveEnrollment() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + summerPlan.getId() + "}")).andExpect(status().isOk()).andExpect(jsonPath("$.id", is(enrollment.getId().intValue()))).andExpect(jsonPath("$.semesterPlanId", is(summerPlan.getId().intValue()))).andExpect(jsonPath("$.moduleOfferingId", is(summerOffering.getId().intValue()))).andExpect(jsonPath("$.moduleId", is(movableModule.getId().intValue())));

        // Gleiche Belegung (gleiche id), nur umgehaengt - nicht geloescht und neu angelegt
        assertThat(enrollmentRepository.findByIdAndSemesterPlanId(enrollment.getId(), winterPlan.getId())).isEmpty();
        assertThat(enrollmentRepository.findByIdAndSemesterPlanId(enrollment.getId(), summerPlan.getId())).isPresent();
    }

    @Test
    void moveEnrollment_WhenEnrollmentUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(put(moveUrl(winterPlan.getId(), 999999L)).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + summerPlan.getId() + "}")).andExpect(status().isNotFound());
    }

    // gehoert aber zu fremdem Plan" ab - beides muss 404 sein ===
    @Test
    void moveEnrollment_WhenEnrollmentBelongsToAnotherPlan_ShouldReturnNotFound() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        // enrollmentId existiert, gehoert aber nicht zu summerPlan -> 404, nicht versehentlich verschoben
        mockMvc.perform(put(moveUrl(summerPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + planWithoutModuleOfferings.getId() + "}")).andExpect(status().isNotFound());

        assertThat(enrollmentRepository.findByIdAndSemesterPlanId(enrollment.getId(), winterPlan.getId())).isPresent();
    }

    // === NEU ===
    @Test
    void moveEnrollment_WhenTargetPlanUnknown_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": 999999}")).andExpect(status().isBadRequest());
    }

    @Test
    void moveEnrollment_WhenSourceAndTargetPlanAreIdentical_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + winterPlan.getId() + "}")).andExpect(status().isBadRequest());
    }

    // === NEU: deckt den AK-Satzteil "Ziel-Fachsemester ungueltig (nicht vorhanden)" ab ===
    @Test
    void moveEnrollment_WhenModuleNotOfferedInTargetSemester_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + planWithoutModuleOfferings.getId() + "}")).andExpect(status().isBadRequest());
    }

    @Test
    void moveEnrollment_WhenModuleAlreadyPlannedInTargetPlan_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);
        // Modul ist im Zielplan schon eingeplant -> Duplikat
        createEnrollment(summerPlan, summerOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{\"targetSemesterPlanId\": " + summerPlan.getId() + "}")).andExpect(status().isBadRequest());
    }

    @Test
    void moveEnrollment_WhenRequestBodyMissing_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
    }

    @Test
    void moveEnrollment_WhenTargetSemesterPlanIdMissingInBody_ShouldReturnBadRequest() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(put(moveUrl(winterPlan.getId(), enrollment.getId())).contentType(MediaType.APPLICATION_JSON).content("{}")).andExpect(status().isBadRequest());
    }

    @Test
    void deleteEnrollment_WhenExists_ShouldReturnOkAndRemoveOnlyTheEnrollment() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(delete(deleteUrl(winterPlan.getId(), enrollment.getId()))).andExpect(status().isOk());

        // Belegung weg ...
        assertThat(enrollmentRepository.findById(enrollment.getId())).isEmpty();
        // ... aber Modulangebot und Modul bestehen weiterhin (AK-Kriterium)
        assertThat(moduleOfferingRepository.findById(winterOffering.getId())).isPresent();
        assertThat(moduleRepository.findById(movableModule.getId())).isPresent();
    }

    // "Nach dem Entfernen liefert GET /semesterplans den Semesterplan ohne das Modul." ===
    @Test
    void deleteEnrollment_WhenExists_GetSemesterPlansShouldNoLongerContainTheModule() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(delete(deleteUrl(winterPlan.getId(), enrollment.getId()))).andExpect(status().isOk());

        // AK-Kriterium: "Nach dem Entfernen liefert GET /semesterplans den Semesterplan ohne das Modul."
        mockMvc.perform(get("/semesterplans")).andExpect(status().isOk()).andExpect(jsonPath("$[?(@.id == " + winterPlan.getId() + ")][0].modules", hasSize(0)));
    }

    // POST-Endpunkt wieder eingeplant werden" ab ===
    @Test
    void deleteEnrollment_WhenExists_ModuleCanBeReEnrolledViaPost() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        mockMvc.perform(delete(deleteUrl(winterPlan.getId(), enrollment.getId()))).andExpect(status().isOk());

        mockMvc.perform(post(url()).contentType(MediaType.APPLICATION_JSON).content("{\"moduleId\": " + movableModule.getId() + "}")).andExpect(status().isCreated()).andExpect(jsonPath("$.moduleId", is(movableModule.getId().intValue())));
    }

    @Test
    void deleteEnrollment_WhenAlreadyDeleted_ShouldReturnNotFound() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);
        enrollmentRepository.delete(enrollment);

        mockMvc.perform(delete(deleteUrl(winterPlan.getId(), enrollment.getId()))).andExpect(status().isNotFound());
    }

    // === NEU: "Fremder Eintrag" aus dem Nachweis-Abschnitt - ID existiert,
    // gehoert aber zu einem anderen Plan ===
    @Test
    void deleteEnrollment_WhenEnrollmentBelongsToAnotherPlan_ShouldReturnNotFound() throws Exception {
        Enrollment enrollment = createEnrollment(winterPlan, winterOffering);

        // enrollmentId existiert, gehoert aber nicht zu summerPlan -> 404, nicht versehentlich geloescht
        mockMvc.perform(delete(deleteUrl(summerPlan.getId(), enrollment.getId()))).andExpect(status().isNotFound());

        assertThat(enrollmentRepository.findById(enrollment.getId())).isPresent();
    }

    // === NEU: deckt AK "Ungueltige Eingabe Status 400" ab ===
    @Test
    void deleteEnrollment_WhenIdIsNotNumeric_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(delete("/semesterplans/" + winterPlan.getId() + "/enrollments/abc")).andExpect(status().isBadRequest());
    }
}
