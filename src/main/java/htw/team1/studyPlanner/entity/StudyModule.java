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

	@Column(name="sws", nullable=false)
	private Integer sws;

	@Enumerated(EnumType.STRING)
	@Column(name="art")
	private ModulArt art;

	@Enumerated(EnumType.STRING)
	@Column(name="turnus")
	private ModulTurnus turnus;
	
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

	public Integer getSws()
	{
		return this.sws;
	}

	public void setSws(Integer sws)
	{
		this.sws = sws;
	}

	public ModulArt getArt()
	{
		return this.art;
	}

	public void setArt(ModulArt art)
	{
		this.art = art;
	}

	public ModulTurnus getTurnus()
	{
		return this.turnus;
	}

	public void setTurnus(ModulTurnus turnus)
	{
		this.turnus = turnus;
	}
	
}
