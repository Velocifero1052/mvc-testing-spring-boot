package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService studentService;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model m) {
		var collegeStudents = studentService.getGradebook();
		m.addAttribute(collegeStudents);
		return "index";
	}

	@PostMapping(value = "/")
	public String createStudent(@ModelAttribute("student")CollegeStudent student, Model m) {
		studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
		var collegeStudent = studentService.getGradebook();
		m.addAttribute("students", collegeStudent);
		return "index";
	}

	@GetMapping("/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model m){
		if(studentService.checkIfStudentIsNull(id)){
			return "error";
		}
		studentService.deleteStudent(id);
		var collegeStudent = studentService.getGradebook();
		m.addAttribute("students", collegeStudent);
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
		public String studentInformation(@PathVariable int id, Model m) {
		return "studentInformation";
		}

}
