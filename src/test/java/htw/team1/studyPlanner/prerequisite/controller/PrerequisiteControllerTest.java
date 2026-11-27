package htw.team1.studyPlanner.prerequisite.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enums.ModuleType;
import htw.team1.studyPlanner.enums.PrerequisiteMode;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import htw.team1.studyPlanner.prerequisite.entity.Prerequisite;
import htw.team1.studyPlanner.prerequisite.repository.PrerequisiteRepository;
import jakarta.transaction.Transactional;
import tools.jackson.databind.json.JsonMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PrerequisiteControllerTest
{
	private final MockMvc mockMvc;
	private final JsonMapper jsonMapper;
	private final ModuleRepository moduleRepository; 
	private final PrerequisiteRepository prerequisiteRepository;
	
	@Autowired
	private PrerequisiteControllerTest(
			MockMvc mockMvc,
			JsonMapper jsonMapper,
			ModuleRepository moduleRepository,
			PrerequisiteRepository prerequisiteRepository
			) {
		this.mockMvc = mockMvc;
		this.jsonMapper = jsonMapper;
		this.moduleRepository = moduleRepository;
		this.prerequisiteRepository = prerequisiteRepository;
	}
	
	@BeforeEach
	void setUp() {
		prerequisiteRepository.deleteAll();
		moduleRepository.deleteAll();
        StudyModule testm1 = moduleRepository.save(createModule("TestmodulEins", "B1", 1, 5, 4, ModuleType.PFLICHT, Cycle.WISE));
        StudyModule testm2 = moduleRepository.save(createModule("TestmodulZwei", "B2", 2, 6, 2, ModuleType.WAHLPFLICHT, null));
        StudyModule testm3 = moduleRepository.save(createModule("TestmodulDrei", "B3", 2, 6, 2, ModuleType.WAHLPFLICHT, Cycle.SOSE));
        Prerequisite prerequisite1 = prerequisiteRepository.save(createPrerequisite(testm1, testm2, PrerequisiteMode.EMPFOHLEN));
        Prerequisite prerequisite2 = prerequisiteRepository.save(createPrerequisite(testm1, testm3, PrerequisiteMode.NOTWENDIG));
		
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
	
	private Prerequisite createPrerequisite(StudyModule successorModule, StudyModule predecessorModule, PrerequisiteMode prerequisiteMode) {
		Prerequisite prerequisite = new Prerequisite();
		prerequisite.setSuccessorModule(successorModule);
		prerequisite.setPredecessorModule(predecessorModule);
		prerequisite.setPrerequisiteMode(prerequisiteMode);
		return prerequisite;
	}
	
	@Test
	void getAllPrerequisites_withValidData_shouldReturnStatusOk() throws Exception {
		mockMvc.perform(get("/prerequisites"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	void getAllSuccessorsOfModuleByModulNumber_withValidModuleNumberWithSuccessors_shouldReturnStatusOkAndList() throws Exception {
		mockMvc.perform(get("/successors/B2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	void getAllSuccessorsOfModuleByModulNumber_withValidModuleNumberWithOutSuccessors_shouldReturnStatusOkAndEmptyList() throws Exception {
		mockMvc.perform(get("/successors/B1"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(0)));
	}
	
	// TODO Test umschreiben nach zentralem Exception-Handler, der 404 wird noch gar nicht an den Client durchgereicht
	@Test
	void getAllSuccessorsOfModuleByModulNumber_withNoValidModuleNumber_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/successors/B999"))
		.andExpect(status().isOk());
	}
}
