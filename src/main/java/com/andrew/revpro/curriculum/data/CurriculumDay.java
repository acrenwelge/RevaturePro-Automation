package com.andrew.revpro.curriculum.data;

import java.time.DayOfWeek;
import java.util.List;

public class CurriculumDay {
	private DayOfWeek dayOfWeek;
	private List<Activity> activities;
	
	public DayOfWeek getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(DayOfWeek dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public List<Activity> getActivities() {
		return activities;
	}
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((dayOfWeek == null) ? 0 : dayOfWeek.hashCode());
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
		CurriculumDay other = (CurriculumDay) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (dayOfWeek != other.dayOfWeek)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CurriculumDay [dayOfWeek=" + dayOfWeek + ", activities=" + activities + "]";
	}
	public CurriculumDay(DayOfWeek dayOfWeek, List<Activity> activities) {
		super();
		this.dayOfWeek = dayOfWeek;
		this.activities = activities;
	}
	
	public CurriculumDay() {}
}
