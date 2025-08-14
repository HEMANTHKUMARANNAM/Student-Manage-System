package com.example.demo12.service;

import com.example.demo12.model.Course;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile = new File("src/main/resources/courses.json");

    public List<Course> getAllCourses() throws IOException {
        if (!jsonFile.exists() || jsonFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(jsonFile, new TypeReference<List<Course>>() {});
    }

    public Course getCourseById(String id) throws IOException {
        return getAllCourses().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void saveCourse(Course course) throws IOException {
        List<Course> courses = getAllCourses();
        courses.add(course);
        objectMapper.writeValue(jsonFile, courses);
    }

    public void updateCourse(Course course) throws IOException {
        List<Course> courses = getAllCourses();
        List<Course> updatedCourses = courses.stream()
                .map(c -> c.getId().equals(course.getId()) ? course : c)
                .collect(Collectors.toList());
        objectMapper.writeValue(jsonFile, updatedCourses);
    }

    public void deleteCourse(String id) throws IOException {
        List<Course> courses = getAllCourses();
        courses.removeIf(c -> c.getId().equals(id));
        objectMapper.writeValue(jsonFile, courses);
    }
}
