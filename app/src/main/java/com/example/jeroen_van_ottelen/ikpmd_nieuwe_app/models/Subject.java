package com.example.jeroen_van_ottelen.ikpmd_nieuwe_app.models;

/**
 * @author Richard Jongenburger
 */

public class Subject {

	private String name;
	private int ects;
	private int period;
	private int grade;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getEcts()
	{
		return ects;
	}

	public void setEcts(int ects)
	{
		this.ects = ects;
	}

	public int getPeriod()
	{
		return period;
	}

	public void setPeriod(int period)
	{
		this.period = period;
	}

	public int getGrade()
	{
		return grade;
	}

	public void setGrade(int grade)
	{
		this.grade = grade;
	}
}
