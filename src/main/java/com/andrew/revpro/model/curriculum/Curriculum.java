package com.andrew.revpro.model.curriculum;

import java.util.List;
import java.util.Set;

public class Curriculum {
	private String name;
	private Set<String> tags;
	private CurriculumType currType;
	private ProgramType progType;
	private List<CurriculumWeek> weeks;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	public CurriculumType getCurrType() {
		return currType;
	}
	public void setCurrType(CurriculumType currType) {
		this.currType = currType;
	}
	public ProgramType getProgType() {
		return progType;
	}
	public void setProgType(ProgramType progType) {
		this.progType = progType;
	}
	public List<CurriculumWeek> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<CurriculumWeek> weeks) {
		this.weeks = weeks;
	}
	public Curriculum(String name, Set<String> tags, CurriculumType currType, ProgramType progType,
			List<CurriculumWeek> weeks) {
		super();
		this.name = name;
		this.tags = tags;
		this.currType = currType;
		this.progType = progType;
		this.weeks = weeks;
	}
	
	public Curriculum() {}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currType == null) ? 0 : currType.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((progType == null) ? 0 : progType.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((weeks == null) ? 0 : weeks.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Curriculum other = (Curriculum) obj;
		if (currType != other.currType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (progType != other.progType)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (weeks == null) {
			if (other.weeks != null)
				return false;
		} else if (!weeks.equals(other.weeks))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Curriculum [name=" + name + ", tags=" + tags + ", currType=" + currType + ", progType=" + progType
				+ ", weeks=" + weeks + "]";
	}
	
}
