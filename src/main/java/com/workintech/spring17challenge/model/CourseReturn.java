package com.workintech.spring17challenge.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
public class CourseReturn {
    private Course course;
    private int totalGpa;
}
