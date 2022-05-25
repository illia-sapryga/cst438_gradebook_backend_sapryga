package com.cst438.services;


import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.cst438.domain.Course;
import com.cst438.domain.CourseDTOG;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;


public class RegistrationServiceMQ extends RegistrationService {

	@Autowired
	EnrollmentRepository enrollmentRepository;

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Value("${registration.url}") 
	String registration_url;

	public RegistrationServiceMQ() {
		System.out.println("MQ registration service ");
	}

	// ----- configuration of message queues

	@Autowired
	Queue registrationQueue;


	// ----- end of configuration of message queue

	// receiver of messages from Registration service
	
	@RabbitListener(queues = "gradebook-queue")
	@Transactional
	public void receive(EnrollmentDTO enrollmentDTO) {
		Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(enrollmentDTO.studentEmail, enrollmentDTO.course_id);
		if (enrollment==null) {
			enrollment = new Enrollment();
			enrollment.setStudentEmail(enrollmentDTO.studentEmail);
			enrollment.setStudentName(enrollmentDTO.studentName);
			Course course=courseRepository.findById(enrollmentDTO.course_id).orElse(null);
			enrollment.setCourse(course);
			enrollmentRepository.save(enrollment);
		}
	}

	// sender of messages to Registration Service
	@Override
	public void sendFinalGrades(int course_id, CourseDTOG courseDTO) {
		 
		this.rabbitTemplate.convertAndSend(registrationQueue, courseDTO.grades);
		
	}

}
