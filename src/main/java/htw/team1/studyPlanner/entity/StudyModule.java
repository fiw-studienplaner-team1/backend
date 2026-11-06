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
	private Integer ects;
	
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

	public Integer getEcts()
	{
		return this.ects;
	}

	public void setEcts(Integer ects)
	{
		this.ects = ects;
	}
	
}
