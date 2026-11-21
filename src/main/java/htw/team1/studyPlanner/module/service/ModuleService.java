package htw.team1.studyPlanner.module.service;

import htw.team1.studyPlanner.module.dto.ModuleRequestDTO;
import htw.team1.studyPlanner.module.dto.ModuleResponseDTO;
import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.module.repository.ModuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    // Create TODO brauchen wir create ? soll man ein module im frontend anlegen koennen? und wenn ja sollte es in den Controller rein
    public ModuleResponseDTO createModule(ModuleRequestDTO request) {
        StudyModule studyModule = new StudyModule();
        studyModule.setName(request.name());
        studyModule.setRecommendedSemester(request.recommendedSemester());
        studyModule.setEcts(request.ects());
        studyModule.setCycle(request.cycle());
        studyModule.setModuleForm(request.moduleForm());
        studyModule.setModuleType(request.moduleType());
        studyModule.setModuleNumber(request.moduleNumber());
        studyModule.setWeeklyHours(request.weeklyHours());

        StudyModule saved = moduleRepository.save(studyModule);
        return toResponse(saved);
    }

    // Read - alle Module
    public List<ModuleResponseDTO> findAllModules() {
        return moduleRepository.findAll().stream()
                .map(ModuleService::toResponse)
                .toList();
    }

    // Read - ein Modul
    public ModuleResponseDTO findById(Long id) {
        StudyModule studyModule = findStudyModuleOrThrow(id);
        return toResponse(studyModule);
    }

    // Read - Modul NOT_FOUND (404)
    private StudyModule findStudyModuleOrThrow(Long id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + id + " nicht gefunden."));
    }

    public static ModuleResponseDTO toResponse(StudyModule studyModule) {
        return new ModuleResponseDTO(
                studyModule.getId(),
                studyModule.getName(),
                studyModule.getModuleNumber(),
                studyModule.getRecommendedSemester(),
                studyModule.getEcts(),
                studyModule.getWeeklyHours(),
                studyModule.getModuleType(),
                studyModule.getCycle(),
                studyModule.getModuleForm()
        );
    }
}
