package com.andrew.revpro.curriculum.data;

import java.util.List;

public class CurriculumWeek {
	private String name;
	private String subtitle;
	private List<String> topics;
	private List<String> environments;
	private List<CurriculumDay> days;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public List<String> getTopics() {
		return topics;
	}
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	public List<String> getEnvironments() {
		return environments;
	}
	public void setEnvironments(List<String> environments) {
		this.environments = environments;
	}
	public List<CurriculumDay> getDays() {
		return days;
	}
	public void setDays(List<CurriculumDay> days) {
		this.days = days;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((days == null) ? 0 : days.hashCode());
		result = prime * result + ((environments == null) ? 0 : environments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((subtitle == null) ? 0 : subtitle.hashCode());
		result = prime * result + ((topics == null) ? 0 : topics.hashCode());
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
		CurriculumWeek other = (CurriculumWeek) obj;
		if (days == null) {
			if (other.days != null)
				return false;
		} else if (!days.equals(other.days))
			return false;
		if (environments == null) {
			if (other.environments != null)
				return false;
		} else if (!environments.equals(other.environments))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (subtitle == null) {
			if (other.subtitle != null)
				return false;
		} else if (!subtitle.equals(other.subtitle))
			return false;
		if (topics == null) {
			if (other.topics != null)
				return false;
		} else if (!topics.equals(other.topics))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CurriculumWeek [name=" + name + ", subtitle=" + subtitle + ", topics=" + topics + ", environments="
				+ environments + ", days=" + days + "]";
	}
	public CurriculumWeek(String name, String subtitle, List<String> topics, List<String> environments,
			List<CurriculumDay> days) {
		super();
		this.name = name;
		this.subtitle = subtitle;
		this.topics = topics;
		this.environments = environments;
		this.days = days;
	}
	
	public CurriculumWeek() {}
}
