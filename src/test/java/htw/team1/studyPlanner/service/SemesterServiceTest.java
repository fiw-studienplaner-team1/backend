package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.ModulTurnus;
import htw.team1.studyPlanner.repository.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class SemesterServiceTest {

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private SemesterRepository semesterRepository;

    @BeforeEach
    void setUp() {
        semesterRepository.deleteAll();
    }

    @Test
    void getOrCreate_WhenSummerSemesterMissing_ShouldCreateWithCorrectDates() {
        Semester result = semesterService.getOrCreate(ModulTurnus.SOSE, 2029);

        assertThat(result.getSemesterId()).isNotNull();
        assertThat(result.getStart()).isEqualTo(LocalDate.of(2029, 4, 1));
        assertThat(result.getEnd()).isEqualTo(LocalDate.of(2029, 9, 30));
    }

    @Test
    void getOrCreate_WhenWinterSemesterMissing_ShouldCreateWithCorrectDates(){
        Semester result = semesterService.getOrCreate(ModulTurnus.WISE,  2029);

        assertThat(result.getSemesterId()).isNotNull();
        assertThat(result.getStart()).isEqualTo(LocalDate.of(2029, 10,1));
        assertThat(result.getEnd()).isEqualTo(LocalDate.of(2030, 3,31));
    }

    @Test
    void getOrCreate_WhenSemesterAlreadyExists_ShouldReturnExistingWithoutDuplicate(){
        Semester result1 = semesterService.getOrCreate(ModulTurnus.WISE, 2029);
        Semester result2 = semesterService.getOrCreate(ModulTurnus.WISE, 2029);

        assertThat(result2.getSemesterId()).isEqualTo(result1.getSemesterId());
        assertThat(semesterRepository.count()).isEqualTo(1L);

    }
    @Test
    void getOrCreate_WhenTypeIsSonst_ShouldThrowException() {
        assertThatThrownBy(() -> semesterService.getOrCreate(ModulTurnus.SONST, 2029))
                .isInstanceOf(IllegalArgumentException.class);

    }
}