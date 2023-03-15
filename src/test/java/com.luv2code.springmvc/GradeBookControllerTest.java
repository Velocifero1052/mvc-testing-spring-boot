package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.ModelAndViewAssert.assertViewName;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentDao studentDao;

    @Mock
    private StudentAndGradeService studentAndGradeService;

    private static MockHttpServletRequest request;

    @BeforeAll
    public static void setup(){
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "Chad");
        request.setParameter("lastname", "Darby");
        request.setParameter("email", "chad.darby@luv2code_school.com");
    }

    @BeforeEach
    void insertSampleData(){
        jdbc.execute("insert into student(id, firstname, lastname, email_address)" +
                "values (1, 'Eric', 'Roby', 'eric.roby@luv2code_school.com')");
    }

    @AfterEach
    void afterEach(){
        jdbc.execute("delete from student");
    }

    @Test
    public void getStudentsHttpRequest() throws Exception{
        var studentOne = new GradebookCollegeStudent("Eric", "Roby",
                "eric.roby@luv2code_school.com");
        var studentTwo = new GradebookCollegeStudent("Chad", "Darby",
                "chad.darby@luv2code_school.com");
        List<CollegeStudent> collegeStudentList =
                new ArrayList<>(Arrays.asList(studentOne, studentTwo));

        when(studentAndGradeService.getGradebook()).thenReturn(collegeStudentList);
        assertIterableEquals(collegeStudentList, studentAndGradeService.getGradebook());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk()).andReturn();

        var mav = mvcResult.getModelAndView();
        assert mav != null;
        assertViewName(mav, "index");
    }


    @Test
    void deleteStudentHttpRequest() throws Exception{
        assertTrue(studentDao.findById(1).isPresent());
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
                .andExpect(status().isOk()).andReturn();
        assertViewName(Objects.requireNonNull(mvcResult.getModelAndView()), "index");
        assertFalse(studentDao.findById(1).isPresent());
    }

    @Test
    void deleteStudentHttpRequestErrorPage() throws Exception {
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}", 0))
                .andExpect(status().isOk()).andReturn();

        var mav = mvcResult.getModelAndView();
        assertViewName(mav, "error");
    }

    @Test
    void createStudentHttpRequest() throws Exception{
        var mvcResult = this.mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("email", request.getParameterValues("email")))
                .andExpect(status().isOk()).andReturn();

        var mav = mvcResult.getModelAndView();
        assert mav != null;
        assertViewName(mav, "index");
        var verifyStudent = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");
        assertNotNull(verifyStudent);
    }

}
