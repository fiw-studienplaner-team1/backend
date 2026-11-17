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

	// Die ID (z. B. "B11") wird nur beim Anlegen gesetzt und kann danach nicht mehr geaendert werden.
// Der leere Konstruktor wird von JPA benoetigt.
	// TODO: Evtl. wird die ID spaeter von der Datenbank generiert (Long statt String, @GeneratedValue).
//       Dann entfaellt dieser Konstruktor mit (String id), und das Kuerzel (z. B. "B11") kommt z.B. in ein eigenes Feld.
	public StudyModule(String id) {
		this.id = id;
	}

	public String getId()
	{
		return this.id;
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
