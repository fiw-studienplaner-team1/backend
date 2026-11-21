package htw.team1.studyPlanner.module.controller;

import htw.team1.studyPlanner.module.dto.ModuleResponseDTO;
import htw.team1.studyPlanner.module.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/modules")
public class ModuleController
{
    // Logger für Meldungen in Konsole/Terminal bzw. Log-Dateien
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    //Instanzvariable
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService)
    {
        this.moduleService = moduleService; //Instanzvariable = Parametervariable
    }

    //GET all modules
    @GetMapping
    public ResponseEntity<List<ModuleResponseDTO>> getAllModules()
    {
        LOG.info("Alle Module werden abgerufen");

        List<ModuleResponseDTO> modules = this.moduleService.findAllModules();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modules);
    }


    //GET modules by id
    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponseDTO> getOneModuleById(@PathVariable Long id)
    {
        LOG.info("Modul mit der id {} wird abgerufen", id);

        ModuleResponseDTO module = this.moduleService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(module);
    }
}
