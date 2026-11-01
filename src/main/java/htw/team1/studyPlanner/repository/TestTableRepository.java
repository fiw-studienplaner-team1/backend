package htw.team1.studyPlanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import htw.team1.studyPlanner.entity.TestTable;

public interface TestTableRepository extends JpaRepository<TestTable, Long>
{

}
