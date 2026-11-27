package htw.team1.studyPlanner.module.entity;

import java.util.List;

import htw.team1.studyPlanner.enums.Cycle;
import htw.team1.studyPlanner.enums.ModuleForm;
import htw.team1.studyPlanner.enums.ModuleType;
import htw.team1.studyPlanner.prerequisite.entity.Prerequisite;
import jakarta.persistence.*;

@Entity
@Table(name="modules")
public class StudyModule
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "module_number", nullable = false, unique = true)
	private String moduleNumber;

	@Column(name = "recommended_semester")
	private Integer recommendedSemester;

	@Column(name = "ects", nullable = false)
	private Integer ects;

	@Column(name = "weekly_hours", nullable = false)
	private Integer weeklyHours;

	@Enumerated(EnumType.STRING)
	@Column(name = "module_type")
	private ModuleType moduleType;

	@Enumerated(EnumType.STRING)
	@Column(name = "cycle")
	private Cycle cycle;

	@Enumerated(EnumType.STRING)
	@Column(name = "module_form")
	private ModuleForm moduleForm;
	
	
	@OneToMany(mappedBy="successorModule")
	List<Prerequisite> successors;
	
	@OneToMany(mappedBy="predecessorModule")
    List<Prerequisite> predecessors;

	public StudyModule() {}

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getModuleNumber()
    {
        return moduleNumber;
    }

    public void setModuleNumber(String moduleNumber)
    {
        this.moduleNumber = moduleNumber;
    }

    public Integer getRecommendedSemester()
    {
        return recommendedSemester;
    }

    public void setRecommendedSemester(Integer recommendedSemester)
    {
        this.recommendedSemester = recommendedSemester;
    }

    public Integer getEcts()
    {
        return ects;
    }

    public void setEcts(Integer ects)
    {
        this.ects = ects;
    }

    public Integer getWeeklyHours()
    {
        return weeklyHours;
    }

    public void setWeeklyHours(Integer weeklyHours)
    {
        this.weeklyHours = weeklyHours;
    }

    public ModuleType getModuleType()
    {
        return moduleType;
    }

    public void setModuleType(ModuleType moduleType)
    {
        this.moduleType = moduleType;
    }

    public Cycle getCycle()
    {
        return cycle;
    }

    public void setCycle(Cycle cycle)
    {
        this.cycle = cycle;
    }

    public ModuleForm getModuleForm()
    {
        return moduleForm;
    }

    public void setModuleForm(ModuleForm moduleForm)
    {
        this.moduleForm = moduleForm;
    }
}
