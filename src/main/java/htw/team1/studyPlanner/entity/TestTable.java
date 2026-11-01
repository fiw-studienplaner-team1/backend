package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(name="test-table")
public class TestTable
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="testdata")
	private String testdata;
	
	public TestTable() {}
	
	public TestTable(String testdata)
	{
		this.testdata = testdata;
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTestdata()
	{
		return this.testdata;
	}

	public void setTestdata(String testdata)
	{
		this.testdata = testdata;
	}
	
}
