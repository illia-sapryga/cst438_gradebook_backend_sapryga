package com.cst438.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;

@RestController
@Transactional
@CrossOrigin(origins = "http://localhost:3000")
public class AssignmentController {
	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@PostMapping("/assignment")
	public AssignmentDTO create(@RequestBody AssignmentDTO assignmentDTO) {
		Course course = courseRepository.findById(assignmentDTO.course_id).orElse(null);
		if (course!=null) {
			Assignment assignment = new Assignment();
			assignment.setName(assignmentDTO.name);
			assignment.setDueDate(assignmentDTO.dueDate);
			assignment.setCourse(course);
			assignment.setNeedsGrading(assignmentDTO.needsGrading);
			assignmentRepository.save(assignment);
			assignmentDTO.id=assignment.getId();
			return assignmentDTO;
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No course with ID " + assignmentDTO.course_id + " was found.");
		}
	}
	
	@PutMapping("/assignment/{id}")
	public void update(@RequestBody AssignmentDTO assignmentDTO, @PathVariable("id") Integer assignmentId ) {
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if (assignment!=null) {
			assignment.setName(assignmentDTO.name);
			assignmentRepository.save(assignment);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No assignment with ID " + assignmentId + " was found.");
		}
	}
	
	@DeleteMapping("/assignment/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Integer assignmentId ) {
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if (assignment!=null) {
			assignmentRepository.delete(assignment);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No assignment with ID " + assignmentId + " was found.");
		}
	}
}