package com.example.demo12.controller;

import com.example.demo12.model.Course;
import com.example.demo12.model.AttendanceRecord;
import com.example.demo12.model.Student;
import com.example.demo12.service.AttendanceService;
import com.example.demo12.service.CourseService;
import com.example.demo12.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public String listCourses(Model model) throws IOException {
        model.addAttribute("listCourses", courseService.getAllCourses());
        return "list_courses";
    }

    @GetMapping("/showNewCourseForm")
    public String showNewCourseForm(Model model) {
        Course course = new Course();
        model.addAttribute("course", course);
        return "new_course";
    }

    @PostMapping("/saveCourse")
    public String saveCourse(@ModelAttribute("course") Course course) throws IOException {
        if (course.getId() == null || course.getId().isEmpty()) {
            course.setId(String.valueOf(System.currentTimeMillis())); // Simple ID generation
            courseService.saveCourse(course);
        } else {
            courseService.updateCourse(course);
        }
        return "redirect:/courses";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "update_course";
    }

    @GetMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable(value = "id") String id) throws IOException {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }

    @GetMapping("/{id}")
    public String viewCourseDetails(@PathVariable("id") String id, Model model) throws IOException {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);

        if (course != null) {
            List<AttendanceRecord> attendanceRecords = attendanceService.getAttendanceByCourseId(id);
            model.addAttribute("attendanceRecords", attendanceRecords);

            Map<String, String> studentNames = studentService.getAllStudents().stream()
                    .collect(Collectors.toMap(Student::getId, Student::getName));
            model.addAttribute("studentNames", studentNames);
        }

        return "course_details";
    }
}
