package htw.team1.studyPlanner.controller;

import htw.team1.studyPlanner.entity.StudyModule;
import htw.team1.studyPlanner.repository.ModuleRepository;
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

    @BeforeEach
    void setUp() {
        moduleRepository.deleteAll();
        moduleRepository.save(createModule("TEST1", "Testmodul Eins", 1, 5));
        moduleRepository.save(createModule("TEST2", "Testmodul Zwei", 2, 6));
    }

    private StudyModule createModule(String id, String name, int semester, int ects) {
        StudyModule module = new StudyModule();
        module.setId(id);
        module.setName(name);
        module.setSemester(semester);
        module.setEcts(ects);
        return module;
    }

    @Test
    void getAllModules_ShouldReturnOkAndList() throws Exception {
        mockMvc.perform(get("/modules"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("TEST1")))
                .andExpect(jsonPath("$[1].id", is("TEST2")));
    }

    @Test
    void getModuleById_WhenExists_ShouldReturnOkAndModule() throws Exception {
        mockMvc.perform(get("/modules/TEST2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("TEST2")))
                .andExpect(jsonPath("$.name", is("Testmodul Zwei")))
                .andExpect(jsonPath("$.semester", is(2)))
                .andExpect(jsonPath("$.ects", is(6)));
    }

    @Test
    void getModuleById_WhenUnknown_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/modules/GIBTSNICHT"))
                .andExpect(status().isNotFound());
    }
}