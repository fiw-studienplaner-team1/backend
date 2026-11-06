package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.dto.ModuleRequestDTO;
import htw.team1.studyPlanner.dto.ModuleResponseDTO;
import htw.team1.studyPlanner.entity.StudyModule;
import htw.team1.studyPlanner.repository.ModuleRepository;
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

    // Create
    public ModuleResponseDTO createModule(ModuleRequestDTO request) {
        StudyModule studyModule = new StudyModule();
        studyModule.setId(request.id());
        studyModule.setName(request.name());
        studyModule.setSemester(request.semester());
        studyModule.setEcts(request.ects());

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
    public ModuleResponseDTO findById(String id) {
        StudyModule studyModule = findStudyModuleOrThrow(id);
        return toResponse(studyModule);
    }

    // Read - Modul NOT_FOUND (404)
    private StudyModule findStudyModuleOrThrow(String id) {
        return moduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Modul " + id + " nicht gefunden."));
    }

    public static ModuleResponseDTO toResponse(StudyModule studyModule) {
        return new ModuleResponseDTO(
                studyModule.getId(),
                studyModule.getName(),
                studyModule.getSemester(),
                studyModule.getEcts()
        );
    }
}
