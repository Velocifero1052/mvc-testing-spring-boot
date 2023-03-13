package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;

    public void createStudent(String firstname, String lastname, String email){
        var student = new CollegeStudent(firstname, lastname, email);
        studentDao.save(student);
    }

    public boolean checkIfStudentIsNull(int id){
        return studentDao.findById(id).isPresent();
    }

    public void deleteStudent(int id){
        studentDao.deleteById(id);
    }

    public Iterable<CollegeStudent> getGradebook(){
        return studentDao.findAll();
    }

}
