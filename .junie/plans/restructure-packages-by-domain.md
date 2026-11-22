---
sessionId: session-261121-170744-13es
---

# Geplante Ordnerstruktur

## Aktuelle Struktur
```
src/main/java/htw/team1/studyPlanner/
├── StudyplannerApplication.java
├── config/
│   └── CorsConfig.java
├── controller/
│   ├── ModuleController.java
│   ├── SemesterPlanController.java
│   └── EnrollmentController.java
├── dto/
│   ├── ModuleRequestDTO.java
│   ├── ModuleResponseDTO.java
│   ├── SemesterPlanRequestDTO.java
│   ├── SemesterPlanResponseDTO.java
│   ├── SemesterPlanDetailDTO.java
│   ├── PlannedModuleDTO.java
│   ├── EnrollmentRequestDTO.java
│   └── EnrollmentResponseDTO.java
├── entity/
│   ├── StudyModule.java
│   ├── ModuleType.java
│   ├── ModuleForm.java
│   ├── ModuleOffering.java
│   ├── Semester.java
│   ├── SemesterPlan.java
│   ├── Enrollment.java
│   └── Cycle.java
├── repository/
│   ├── ModuleRepository.java
│   ├── ModuleOfferingRepository.java
│   ├── SemesterRepository.java
│   ├── SemesterPlanRepository.java
│   └── EnrollmentRepository.java
├── service/
│   ├── ModuleService.java
│   ├── SemesterService.java
│   ├── SemesterPlanService.java
│   └── EnrollmentService.java
└── shared/
    └── enums/
        └── Cycle.java
```

## Geplante Struktur
```
src/main/java/htw/team1/studyPlanner/
├── StudyplannerApplication.java
├── config/
│   └── CorsConfig.java
├── enums/
│   ├── Cycle.java          (von module, semester genutzt)
│   ├── ModuleType.java     (von module genutzt)
│   └── ModuleForm.java     (von module genutzt)
├── exceptions/
│   └── (leer, für zukünftige globale Handler)
├── module/
│   ├── controller/
│   │   └── ModuleController.java
│   ├── service/
│   │   └── ModuleService.java
│   ├── repository/
│   │   ├── ModuleRepository.java
│   │   └── ModuleOfferingRepository.java
│   ├── entity/
│   │   ├── StudyModule.java
│   │   └── ModuleOffering.java
│   └── dto/
│       ├── ModuleRequestDTO.java
│       └── ModuleResponseDTO.java
├── semester/
│   ├── service/
│   │   └── SemesterService.java
│   ├── repository/
│   │   └── SemesterRepository.java
│   └── entity/
│       └── Semester.java
├── semesterplan/
│   ├── controller/
│   │   └── SemesterPlanController.java
│   ├── service/
│   │   └── SemesterPlanService.java
│   ├── repository/
│   │   └── SemesterPlanRepository.java
│   ├── entity/
│   │   └── SemesterPlan.java
│   └── dto/
│       ├── SemesterPlanRequestDTO.java
│       ├── SemesterPlanResponseDTO.java
│       ├── SemesterPlanDetailDTO.java
│       └── PlannedModuleDTO.java
└── enrollment/
    ├── controller/
    │   └── EnrollmentController.java
    ├── service/
    │   └── EnrollmentService.java
    ├── repository/
    │   └── EnrollmentRepository.java
    ├── entity/
    │   └── Enrollment.java
    └── dto/
        ├── EnrollmentRequestDTO.java
        └── EnrollmentResponseDTO.java
```

## Test-Struktur (angepasst)
```
src/test/java/htw/team1/studyPlanner/
├── StudyplannerApplicationTests.java
├── module/
│   └── controller/
│       └── ModuleControllerTest.java
├── semesterplan/
│   └── controller/
│       └── SemesterPlanControllerTest.java
├── enrollment/
│   ├── controller/
│   │   └── EnrollmentControllerTest.java
│   └── repository/
│       └── EnrollmentRepositoryTest.java
└── semester/
    └── service/
        └── SemesterServiceTest.java
```

## Bemerkungen
- **Cycle** wird von `shared/enums/` nach `enums/` verschoben (Top-Level Paket)
- **ModuleType** wird von `entity/` nach `enums/` verschoben (Top-Level Paket)
- **ModuleForm** wird von `entity/` nach `enums/` verschoben (Top-Level Paket) — ist ein Enum, keine Entity
- **semester** bekommt keinen Controller, da `SemesterService` direkt über `SemesterPlanController` aufgerufen wird
- **ModuleOffering** und **ModuleOfferingRepository** landen im `module/`-Paket, da sie eng mit StudyModule verbunden sind
- Alle Importe in allen Dateien (main + test) müssen aktualisiert werden
- Die `@Entity`, `@Repository`, `@Service` und `@Controller` Annotationen mit Spring Component Scanning funktionieren automatisch mit Unterverzeichnissen

# Delivery Steps

### ✓ Step 1: Enums in Top-Level enums/ Ordner verschieben
Cycle, ModuleType, und ModuleForm werden in den neuen `enums/`-Ordner an der Top-Level verschoben.
- `enums/`-Ordner erstellen
- Cycle.java von `shared/enums/` nach `enums/` verschieben
- ModuleType.java von `entity/` nach `enums/` verschieben
- ModuleForm.java von `entity/` nach `enums/` verschieben
- Package-Deklaration aller drei Enums ändern zu `htw.team1.studyPlanner.enums`
- Alle Importe in anderen Dateien aktualisieren (StudyModule.java, ModuleService.java, ModuleRequestDTO.java, ModuleResponseDTO.java, ModuleControllerTest.java, u. a.)
- Leere Ordner `shared/enums/` und `shared/` entfernen

### ✓ Step 2: Module-Domäne erstellen
Alle module-bezogenen Dateien in den neuen module/ Ordner strukturieren.
- Ordner module/controller, module/service, module/repository, module/entity, module/dto erstellen
- ModuleController.java → module/controller/
- ModuleService.java → module/service/
- ModuleRepository.java + ModuleOfferingRepository.java → module/repository/
- StudyModule.java, ModuleOffering.java → module/entity/
- ModuleRequestDTO.java + ModuleResponseDTO.java → module/dto/
- Alle Package-Deklarationen und Importe aktualisieren

### ✓ Step 3: Semester-Domäne erstellen
Alle semester-bezogenen Dateien in den neuen semester/ Ordner strukturieren.
- Ordner semester/service, semester/repository, semester/entity erstellen
- SemesterService.java → semester/service/
- SemesterRepository.java → semester/repository/
- Semester.java → semester/entity/
- Alle Package-Deklarationen und Importe aktualisieren

### ✓ Step 4: Semesterplan-Domäne erstellen
Alle semesterplan-bezogenen Dateien in den neuen semesterplan/ Ordner strukturieren.
- Ordner semesterplan/controller, semesterplan/service, semesterplan/repository, semesterplan/entity, semesterplan/dto erstellen
- SemesterPlanController.java → semesterplan/controller/
- SemesterPlanService.java → semesterplan/service/
- SemesterPlanRepository.java → semesterplan/repository/
- SemesterPlan.java → semesterplan/entity/
- SemesterPlanRequestDTO, SemesterPlanResponseDTO, SemesterPlanDetailDTO, PlannedModuleDTO → semesterplan/dto/
- Alle Package-Deklarationen und Importe aktualisieren

### ✓ Step 5: Enrollment-Domäne erstellen
Alle enrollment-bezogenen Dateien in den neuen enrollment/ Ordner strukturieren.
- Ordner enrollment/controller, enrollment/service, enrollment/repository, enrollment/entity, enrollment/dto erstellen
- EnrollmentController.java → enrollment/controller/
- EnrollmentService.java → enrollment/service/
- EnrollmentRepository.java → enrollment/repository/
- Enrollment.java → enrollment/entity/
- EnrollmentRequestDTO.java + EnrollmentResponseDTO.java → enrollment/dto/
- Alle Package-Deklarationen und Importe aktualisieren

### ✓ Step 6: Test-Dateien umziehen und Importe reparieren
Alle Test-Dateien in die passende Domänen-Struktur verschieben und alle Importe korrigieren.
- ModuleControllerTest.java → module/controller/
- SemesterPlanControllerTest.java → semesterplan/controller/
- EnrollmentControllerTest.java → enrollment/controller/
- EnrollmentRepositoryTest.java → enrollment/repository/
- SemesterServiceTest.java → semester/service/
- Alle Importe in allen Test-Dateien auf neue Packages aktualisieren

### ✓ Step 7: Alte Ordner entfernen und kompilieren
Leere alte Ordner entfernen und finales Kompilierungs- und Test-Check durchführen.
- Alte Ordner controller/, dto/, entity/, repository/, service/ entfernen (wenn leer)
- mvn clean test ausführen um alle 19 Tests zu validieren