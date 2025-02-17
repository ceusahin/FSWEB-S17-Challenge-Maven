package com.workintech.spring17challenge.controller;

import com.workintech.spring17challenge.exceptions.ApiException;
import com.workintech.spring17challenge.model.*;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private List<Course> courses;
    private CourseGpa lowCourseGpa;
    private CourseGpa mediumCourseGpa;
    private CourseGpa highCourseGpa;

    @Autowired
    public CourseController(CourseGpa lowCourseGpa, CourseGpa mediumCourseGpa, CourseGpa highCourseGpa) {
        this.lowCourseGpa = lowCourseGpa;
        this.mediumCourseGpa = mediumCourseGpa;
        this.highCourseGpa = highCourseGpa;
    }

    @PostConstruct
    public void init() {
        courses = new ArrayList<>();
    }

    @GetMapping()
    public List<Course> getAllCourses() {
        return courses;
    }

    @GetMapping("/{name}")
    public Course getCourseByName(@PathVariable String name) {
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }
        throw new ApiException("This course is not exist", HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public CourseReturn addCourse(@Validated @RequestBody Course addedCourse) {
        for (Course course : courses) {
            if (course.getName().equals(addedCourse.getName())){
                return null;
            }
        }

        if (addedCourse.getCredit() <= 2) {
            CourseReturn courseReturn = new CourseReturn(addedCourse, addedCourse.getGrade().getCoefficient() * addedCourse.getCredit() * lowCourseGpa.getGpa());
            courses.add(addedCourse);
            return courseReturn;
        } else if (addedCourse.getCredit() == 3) {
            CourseReturn courseReturn = new CourseReturn(addedCourse, addedCourse.getGrade().getCoefficient() * addedCourse.getCredit() * mediumCourseGpa.getGpa());
            courses.add(addedCourse);
            return courseReturn;
        } else if (addedCourse.getCredit() == 4) {
            CourseReturn courseReturn = new CourseReturn(addedCourse, addedCourse.getGrade().getCoefficient() * addedCourse.getCredit() * highCourseGpa.getGpa());
            courses.add(addedCourse);
            return courseReturn;
        }

        throw new ApiException("Wrong situation",HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    public CourseReturn updateCourse(@PathVariable int id, @RequestBody Course course){
        for (Course oldCourse : courses) {
            if (oldCourse.getId() == id) {
                oldCourse.setGrade(course.getGrade());
                oldCourse.setName(course.getName());
                oldCourse.setCredit(course.getCredit());

                if (oldCourse.getCredit() <= 2) {
                    CourseReturn courseReturn = new CourseReturn(oldCourse, oldCourse.getGrade().getCoefficient() * oldCourse.getCredit() * lowCourseGpa.getGpa());
                    return courseReturn;
                } else if (oldCourse.getCredit() == 3) {
                    CourseReturn courseReturn = new CourseReturn(oldCourse, oldCourse.getGrade().getCoefficient() * oldCourse.getCredit() * mediumCourseGpa.getGpa());
                    return courseReturn;
                } else if (oldCourse.getCredit() == 4) {
                    CourseReturn courseReturn = new CourseReturn(oldCourse, oldCourse.getGrade().getCoefficient() * oldCourse.getCredit() * highCourseGpa.getGpa());
                    return courseReturn;
                }
            }
        }
        throw new ApiException("Wrong situation",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                courses.remove(id);
            }
        }
    }
}
