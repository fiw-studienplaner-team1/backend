package htw.team1.studyPlanner.module.controller;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enums.ModuleType;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ModuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository moduleRepository;

    private Long module1Id;
    private Long module2Id;

    @BeforeEach
    void setUp() {
        moduleRepository.deleteAll();
        StudyModule m1 = moduleRepository.save(createModule("Testmodul Eins", "B1", 1, 5, 4, ModuleType.PFLICHT, Cycle.WISE));
        StudyModule m2 = moduleRepository.save(createModule("Testmodul Zwei", "B2", 2, 6, 2, ModuleType.WAHLPFLICHT, null));
        module1Id = m1.getId();
        module2Id = m2.getId();
    }

    private StudyModule createModule(String name, String moduleNumber, int semester, int ects, int weeklyHours, ModuleType moduleType, Cycle cycle) {
        StudyModule module = new StudyModule();
        module.setName(name);
        module.setModuleNumber(moduleNumber);
        module.setRecommendedSemester(semester);
        module.setEcts(ects);
        module.setWeeklyHours(weeklyHours);
        module.setModuleType(moduleType);
        module.setCycle(cycle);
        return module;
    }

    @Test
    void getAllModules_ShouldReturnOkAndList() throws Exception {
        mockMvc.perform(get("/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getModuleById_WhenExists_ShouldReturnOkAndModule() throws Exception {
        mockMvc.perform(get("/modules/" + module2Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Testmodul Zwei")))
                .andExpect(jsonPath("$.moduleNumber", is("B2")))
                .andExpect(jsonPath("$.recommendedSemester", is(2)))
                .andExpect(jsonPath("$.ects", is(6)))
                .andExpect(jsonPath("$.weeklyHours", is(2)))
                .andExpect(jsonPath("$.moduleType", is("WAHLPFLICHT")))
                .andExpect(jsonPath("$.cycle").value(org.hamcrest.Matchers.nullValue()));
    }

    @Test
    void getModuleById_WhenUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/modules/999"))
                .andExpect(status().isNotFound());
    }
}
