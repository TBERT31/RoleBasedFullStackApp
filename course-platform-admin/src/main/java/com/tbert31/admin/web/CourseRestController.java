package com.tbert31.admin.web;

import com.tbert31.admin.dto.CourseDTO;
import com.tbert31.admin.entity.Course;
import com.tbert31.admin.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@CrossOrigin("http://localhost:4200")
public class CourseRestController {

    private CourseService courseService;

    public CourseRestController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public Page<CourseDTO> searchCourses(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ){
        return courseService.findCoursesByCourseName(keyword, page, size);
    }

    @DeleteMapping("/{courseId}")
    public void deleteCourse(@PathVariable Long courseId){
        courseService.removeCourse(courseId);
    }

    @PostMapping
    public CourseDTO saveCourse(@RequestBody CourseDTO courseDTO){
        return courseService.createCourse(courseDTO);
    }

    @PutMapping("/{courseId}")
    public CourseDTO updateCourse(
            @PathVariable Long courseId,
            @RequestBody CourseDTO courseDTO
    ){
        courseDTO.setCourseId(courseId);
        return courseService.updateCourse(courseDTO);
    }

    @PostMapping("/{courseId}/enroll/students/{studentId}")
    public void enrollStudentInCourse(
            @PathVariable Long courseId,
            @PathVariable Long studentId
    ){
        courseService.assignStudentToCourse(courseId, studentId);
    }


}
