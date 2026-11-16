package htw.team1.studyPlanner.service;

import htw.team1.studyPlanner.entity.Semester;
import htw.team1.studyPlanner.entity.SemesterTyp;
import htw.team1.studyPlanner.repository.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

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
        Semester result = semesterService.getOrCreate(SemesterTyp.SOSE, 2029);

        assertThat(result.getSemesterId()).isNotNull();
        assertThat(result.getStart()).isEqualTo(LocalDate.of(2029, 4, 1));
        assertThat(result.getEnd()).isEqualTo(LocalDate.of(2029, 9, 30));
    }

    @Test
    void getOrCreate_WhenWinterSemesterMissing_ShouldCreateWithCorrectDates(){
        Semester result = semesterService.getOrCreate(SemesterTyp.WISE,  2029);

        assertThat(result.getSemesterId()).isNotNull();
        assertThat(result.getStart()).isEqualTo(LocalDate.of(2029, 10,1));
        assertThat(result.getEnd()).isEqualTo(LocalDate.of(2030, 3,31));
    }

    @Test
    void getOrCreate_WhenSemesterAlreadyExists_ShouldReturnExistingWithoutDuplicate(){
        Semester result1 = semesterService.getOrCreate(SemesterTyp.WISE, 2029);
        Semester result2 = semesterService.getOrCreate(SemesterTyp.WISE, 2029);

        assertThat(result2.getSemesterId()).isEqualTo(result1.getSemesterId());
        assertThat(semesterRepository.count()).isEqualTo(1L);

    }
}