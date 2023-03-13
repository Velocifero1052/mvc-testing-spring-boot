package com.luv2code.springmvc.repository;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Student;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StudentDao extends CrudRepository<CollegeStudent, Integer> {

    CollegeStudent findByEmailAddress(String email);

    Boolean existsById(int id);

}
