package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradeDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradesServiceTest {

    @Autowired
    StudentDao studentDao;

    @Autowired
    MathGradeDao mathGradeDao;

    @Autowired
    ScienceGradesDao scienceGradesDao;

    @Autowired
    HistoryGradesDao historyGradesDao;

    @Autowired
    StudentAndGradeService studentAndGradeService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void insertSampleData(){
        jdbcTemplate.execute("insert into student(id, firstname, lastname, email_address)" +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
        jdbcTemplate.execute("insert into math_grade(id, student_id, grade)" +
                "values (1, 1, 99.0)");
        jdbcTemplate.execute("insert into science_grade(id, student_id, grade)" +
                "values (1, 1, 99.0)");
        jdbcTemplate.execute("insert into history_grade(id, student_id, grade)" +
                "values (1, 1, 99.0)");
    }

    @Test
    public void createStudentService(){
        var email = "chad.darby@luv2code_school.com";
        studentAndGradeService.createStudent("Chad", "Darby", email);
        var student = studentDao.findByEmailAddress(email);
        assertEquals(email, student.getEmailAddress());
    }

    @Test
    public void isStudentNullCheck(){
        assertTrue(studentAndGradeService.checkIfStudentIsNull(1));
        assertFalse(studentAndGradeService.checkIfStudentIsNull(0));
    }

    @Test
    public void deleteStudentService(){
        Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
        assertTrue(deletedCollegeStudent.isPresent(), "Return True");
        studentAndGradeService.deleteStudent(1);
        assertFalse(studentAndGradeService.checkIfStudentIsNull(1));
    }

    @Test
    @Sql("/insert_data.sql")
    public void getGradeBookService(){
        Iterable<CollegeStudent> iterableCollegeStudents = studentAndGradeService.getGradebook();
        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for(CollegeStudent collegeStudent: iterableCollegeStudents){
            collegeStudents.add(collegeStudent);
        }
        assertEquals(5, collegeStudents.size());
    }

    @Test
    public void createGradeService(){
        //create the grade page
        assertTrue(studentAndGradeService.createGrade(80.50, 1, "math"));
        assertTrue(studentAndGradeService.createGrade(80.50, 1, "science"));
        assertTrue(studentAndGradeService.createGrade(80.50, 1, "history"));

        //get all grades with studentID
        Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
        Iterable<ScienceGrade> scienceGrades = scienceGradesDao.findScienceGradeByStudentId(1);
        Iterable<HistoryGrade> historyGrades = historyGradesDao.findHistoryGradeByStudentId(1);
        //verify there is grades
        assertTrue(mathGrades.iterator().hasNext());
        assertTrue(scienceGrades.iterator().hasNext());
        assertTrue(historyGrades.iterator().hasNext());
    }

    @Test
    public void createGradeServiceReturnFalse(){
        assertFalse(studentAndGradeService.createGrade(105, 1, "math"));
        assertFalse(studentAndGradeService.createGrade(-1, 1, "math"));
        assertFalse(studentAndGradeService.createGrade(80, 2, "math"));
        assertFalse(studentAndGradeService.createGrade(80, 2, "literature"));

    }

    @AfterEach
    public void setupAfterEachTransaction(){
        jdbcTemplate.execute("delete from student");
        jdbcTemplate.execute("delete from math_grade");
        jdbcTemplate.execute("delete from science_grade");
        jdbcTemplate.execute("delete from history_grade");

    }
}
