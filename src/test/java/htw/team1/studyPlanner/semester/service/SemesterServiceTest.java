package htw.team1.studyPlanner.semester.service;

import htw.team1.studyPlanner.semester.entity.Semester;
import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.semester.repository.SemesterRepository;
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
        Semester result = semesterService.getOrCreate(Cycle.SOSE, 2029);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2029, 4, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2029, 9, 30));
    }

    @Test
    void getOrCreate_WhenWinterSemesterMissing_ShouldCreateWithCorrectDates(){
        Semester result = semesterService.getOrCreate(Cycle.WISE,  2029);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2029, 10,1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2030, 3,31));
    }

    @Test
    void getOrCreate_WhenSemesterAlreadyExists_ShouldReturnExistingWithoutDuplicate(){
        Semester result1 = semesterService.getOrCreate(Cycle.WISE, 2029);
        Semester result2 = semesterService.getOrCreate(Cycle.WISE, 2029);

        assertThat(result2.getId()).isEqualTo(result1.getId());
        assertThat(semesterRepository.count()).isEqualTo(1L);

    }
    @Test
    void getOrCreate_WhenTypeIsSonst_ShouldThrowException() {
        assertThatThrownBy(() -> semesterService.getOrCreate(Cycle.SONST, 2029))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
