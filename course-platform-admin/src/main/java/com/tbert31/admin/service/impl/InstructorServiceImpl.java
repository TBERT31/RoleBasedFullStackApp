package com.tbert31.admin.service.impl;

import com.tbert31.admin.dao.InstructorDao;
import com.tbert31.admin.dto.InstructorDTO;
import com.tbert31.admin.entity.Course;
import com.tbert31.admin.entity.Instructor;
import com.tbert31.admin.entity.User;
import com.tbert31.admin.mapper.InstructorMapper;
import com.tbert31.admin.service.CourseService;
import com.tbert31.admin.service.InstructorService;
import com.tbert31.admin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private InstructorDao instructorDao;

    private InstructorMapper instructorMapper;

    private UserService userService;

    private CourseService courseService;

    public InstructorServiceImpl(InstructorDao instructorDao, InstructorMapper instructorMapper, UserService userService, CourseService courseService) {
        this.instructorDao = instructorDao;
        this.instructorMapper = instructorMapper;
        this.userService = userService;
        this.courseService = courseService;
    }


    @Override
    public Instructor loadInstructorById(Long instructorId) {
        return instructorDao.findById(instructorId).orElseThrow(()->new EntityNotFoundException("Instructor with ID "+instructorId+"Not Found"));
    }

    @Override
    public Page<InstructorDTO> findInstructorByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Instructor> instructorsPage = instructorDao.findInstructorsByName(name, pageRequest);
        return new PageImpl<>(instructorsPage.getContent().stream()
                .map(instructor -> instructorMapper.fromInstructor(instructor))
                .collect(Collectors.toList()), pageRequest, instructorsPage.getTotalElements());
    }

    @Override
    public InstructorDTO loadInstructorByEmail(String email) {
        return instructorMapper.fromInstructor(instructorDao.findInstructorByEmail(email));
    }

    @Override
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        User user = userService.createUser(instructorDTO.getUser().getEmail(), instructorDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Instructor");
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(user);
        Instructor createdInstructor = instructorDao.save(instructor);
        return instructorMapper.fromInstructor(createdInstructor);
    }

    @Override
    public InstructorDTO updateInstructor(InstructorDTO instructorDTO) {
        Instructor loadedInstructor = loadInstructorById(instructorDTO.getInstructorId());
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(loadedInstructor.getUser());
        instructor.setCourses(loadedInstructor.getCourses());
        Instructor updatedInstructor = instructorDao.save(instructor);
        return instructorMapper.fromInstructor(updatedInstructor);
    }

    @Override
    public List<InstructorDTO> fetchInstructors() {
        return instructorDao.findAll().stream()
                .map(instructor -> instructorMapper.fromInstructor(instructor))
                .collect(Collectors.toList());
    }

    @Override
    public void removeInstructor(Long instructorId) {
        Instructor instructor = loadInstructorById(instructorId);
        for(Course course : instructor.getCourses()){
            courseService.removeCourse(course.getCourseId());
        }
        instructorDao.deleteById(instructorId);
    }
}
