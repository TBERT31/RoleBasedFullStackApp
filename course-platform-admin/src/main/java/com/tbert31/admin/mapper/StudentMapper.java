package com.tbert31.admin.mapper;

import com.tbert31.admin.dto.StudentDTO;
import com.tbert31.admin.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {
    public StudentDTO fromStudent(Student student){
        StudentDTO studentDTO = new StudentDTO();
        BeanUtils.copyProperties(student, studentDTO);
        return studentDTO;
    }

    public Student fromStudentDTO(StudentDTO studentDTO){
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        return student;
    }
}
