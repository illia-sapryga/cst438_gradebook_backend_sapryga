package com.cst438.domain;

import java.sql.Date;

public class AssignmentDTO {
	public int id;
	public int course_id;
	public String name;
	public Date dueDate;
	public int needsGrading;
	
	public AssignmentDTO() {
		this.id=0;
		this.course_id=0;
		this.name=null;
		this.dueDate=null;
		this.needsGrading =1;
	}
	
	public AssignmentDTO(int course_id, String name, Date dueDate) {
		this.name=name;
		this.dueDate=dueDate;
		this.course_id=course_id;
	}
	
	public AssignmentDTO(int id, int course_id, String name, Date dueDate) {
		this.id=id;
		this.course_id=course_id;
		this.name=name;
		this.dueDate=dueDate;
	}
}