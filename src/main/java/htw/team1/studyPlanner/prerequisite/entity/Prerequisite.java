package htw.team1.studyPlanner.prerequisite.entity;

import htw.team1.studyPlanner.module.entity.StudyModule;
import htw.team1.studyPlanner.enums.PrerequisiteMode;
import jakarta.persistence.*;

//TODO erweitern um nicht-Modul-Vorraussetzungen
@Entity
@Table(name="module_prerequisites", uniqueConstraints={
	    @UniqueConstraint(
	    		columnNames = {"fk_successor_module_id", "fk_predecessor_module_id"}
	    		)
	    })
public class Prerequisite
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	// hier nicht id von StudyModule, da generierte Ids keine garantierte
	// Verbindung zum konkreten Modul haben
	@ManyToOne
    @JoinColumn(name="fk_successor_module_id", referencedColumnName="module_number", nullable=false)
	private StudyModule successorModule;
	
	@ManyToOne
	@JoinColumn(name="fk_predecessor_module_id", referencedColumnName="module_number")
	private StudyModule predecessorModule;
	
	@Enumerated(EnumType.STRING)
	@Column(name="modus", nullable=false)
	private PrerequisiteMode mode;
	
	public Prerequisite() {}

	public Integer getId()
	{
		return this.id;
	}

	public StudyModule getSuccessorModule()
	{
		return this.successorModule;
	}

	public void setSuccessorModule(StudyModule successorModule)
	{
		this.successorModule = successorModule;
	}

	public StudyModule getPredecessorModule()
	{
		return this.predecessorModule;
	}

	public void setPredecessorModule(StudyModule predecessorModule)
	{
		this.predecessorModule = predecessorModule;
	}

	public PrerequisiteMode getMode()
	{
		return this.mode;
	}

	public void setMode(PrerequisiteMode mode)
	{
		this.mode = mode;
	}
	
	
}
