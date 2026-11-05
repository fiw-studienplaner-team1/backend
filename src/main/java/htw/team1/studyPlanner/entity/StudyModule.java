package htw.team1.studyPlanner.entity;

import jakarta.persistence.*;

@Entity
@Table(name="modules")
public class StudyModule
{
	@Id
	private String id;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="semester")
	private Integer semester;
	
	@Column(name="ects", nullable=false)
	private Integer credits;
	
	public StudyModule() {}

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getSemester()
	{
		return this.semester;
	}

	public void setSemester(Integer semester)
	{
		this.semester = semester;
	}

	public Integer getCredits()
	{
		return this.credits;
	}

	public void setCredits(Integer credits)
	{
		this.credits = credits;
	}
	
}
