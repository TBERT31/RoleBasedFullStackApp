package com.tbert31.admin.service.impl;

import com.tbert31.admin.dao.CourseDao;
import com.tbert31.admin.dao.InstructorDao;
import com.tbert31.admin.dao.StudentDao;
import com.tbert31.admin.dto.CourseDTO;
import com.tbert31.admin.entity.Course;
import com.tbert31.admin.entity.Instructor;
import com.tbert31.admin.entity.Student;
import com.tbert31.admin.mapper.CourseMapper;
import com.tbert31.admin.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private CourseDao courseDao;

    private CourseMapper courseMapper;
    private InstructorDao instructorDao;

    private StudentDao studentDao;

    public CourseServiceImpl(CourseDao courseDao, CourseMapper courseMapper, InstructorDao instructorDao, StudentDao studentDao) {
        this.courseDao = courseDao;
        this.courseMapper = courseMapper;
        this.instructorDao = instructorDao;
        this.studentDao = studentDao;
    }


    @Override
    public Course loadCourseById(Long courseId) {
        return courseDao.findById(courseId).orElseThrow(() -> new EntityNotFoundException("Course with ID "+ courseId +" Not Found"));
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromCourseDTO(courseDTO);
        Instructor instructor = instructorDao
                .findById(courseDTO.getInstructor().getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor with ID "+ courseDTO.getInstructor().getInstructorId() +" Not Found"));
        course.setInstructor(instructor);
        Course createdCourse = courseDao.save(course);
        return courseMapper.fromCourse(createdCourse);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course loadedCourse = loadCourseById(courseDTO.getCourseId());
        Instructor instructor = instructorDao
                .findById(courseDTO.getInstructor().getInstructorId())
                .orElseThrow(() -> new EntityNotFoundException("Instructor with ID "+ courseDTO.getInstructor().getInstructorId() +" Not Found"));
        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());
        Course updatedCourse = courseDao.save(course);
        return courseMapper.fromCourse(updatedCourse);
    }

    @Override
    public Page<CourseDTO> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseDao.findCoursesByCourseNameContains(keyword, pageRequest);
        return new PageImpl<>(coursesPage.getContent().stream()
                .map(course -> courseMapper.fromCourse(course))
                .collect(Collectors.toList()), pageRequest, coursesPage.getTotalElements());
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {
        Student student = studentDao.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student with ID "+ studentId +" Not Found"));;
        Course course = loadCourseById(courseId);
        course.assignStudentToCourse(student);
    }

    @Override
    public Page<CourseDTO> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course>  studentCoursesPage = courseDao.getCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(studentCoursesPage.getContent().stream()
                .map(course -> courseMapper.fromCourse(course))
                .collect(Collectors.toList()), pageRequest, studentCoursesPage.getTotalElements());
    }

    @Override
    public Page<CourseDTO> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> nonEnrolledInCoursesByStudentId = courseDao.getNonEnrolledInCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(nonEnrolledInCoursesByStudentId.getContent().stream()
                .map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),
                pageRequest, nonEnrolledInCoursesByStudentId.getTotalElements());
    }

    @Override
    public void removeCourse(Long courseId) {
        courseDao.deleteById(courseId);
    }

    @Override
    public Page<CourseDTO> fetchCoursesForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> instructorCoursesPage = courseDao.getCoursesByInstructorId(instructorId, pageRequest);
        return new PageImpl<>(instructorCoursesPage.getContent().stream()
                .map(course -> courseMapper.fromCourse(course)).collect(Collectors.toList()),
                pageRequest, instructorCoursesPage.getTotalElements());
    }
}
