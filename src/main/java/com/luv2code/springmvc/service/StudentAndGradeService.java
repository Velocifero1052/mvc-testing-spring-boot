package com.luv2code.springmvc.service;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDao studentDao;
    @Autowired
    private MathGrade mathGrade;

    @Autowired
    private MathGradeDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradesDao;
    @Autowired
    private HistoryGradesDao historyGradesDao;

    @Autowired
    private ScienceGrade scienceGrade;

    @Autowired
    private HistoryGrade historyGrade;

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

    public boolean createGrade(double grade, int studentId, String gradeType){
        if(!checkIfStudentIsNull(studentId))
            return false;
        if(grade >= 0 && grade < 100){
            if(gradeType.equals("math")){
                mathGrade.setId(0);
                mathGrade.setGrade(grade);
                mathGrade.setStudentId(studentId);
                mathGradeDao.save(mathGrade);
            }
            if(gradeType.equals("science")){
                scienceGrade.setId(0);
                scienceGrade.setGrade(grade);
                scienceGrade.setStudentId(studentId);
                scienceGradesDao.save(scienceGrade);
            }
            if(gradeType.equals("history")){
                historyGrade.setId(0);
                historyGrade.setGrade(grade);
                historyGrade.setStudentId(studentId);
                historyGradesDao.save(historyGrade);
            }
        }else return false;
        return true;
    }

}
