package com.tbert31.admin.service;

import com.tbert31.admin.dto.StudentDTO;
import com.tbert31.admin.entity.Student;
import org.springframework.data.domain.Page;

public interface StudentService {
    Student loadStudentById(Long studentId);

    Page<StudentDTO> loadStudentsByName(String name, int page, int size);

    StudentDTO loadStudentByEmail(String email);

    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO updateStudent(StudentDTO studentDTO);

    void removeStudent(Long studentId);
}
