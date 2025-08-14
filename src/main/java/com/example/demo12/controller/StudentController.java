package com.example.demo12.controller;

import com.example.demo12.model.AttendanceRecord;
import com.example.demo12.model.Student;
import com.example.demo12.service.AttendanceService;
import com.example.demo12.service.CourseService;
import com.example.demo12.service.StudentService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(value = "keyword", required = false) String keyword) throws IOException {
        if (keyword != null && !keyword.isEmpty()) {
            model.addAttribute("listStudents", studentService.searchStudents(keyword));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("listStudents", studentService.getAllStudents());
        }
        return "index";
    }

    @GetMapping("/showNewStudentForm")
    public String showNewStudentForm(Model model) {
        Student student = new Student();
        model.addAttribute("student", student);
        return "new_student";
    }

    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute("student") Student student) throws IOException {
        studentService.saveStudent(student);
        return "redirect:/";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable(value = "id") String id, Model model) throws IOException {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        return "update_student";
    }

    @GetMapping("/deleteStudent/{id}")
    public String deleteStudent(@PathVariable(value = "id") String id) throws IOException {
        this.studentService.deleteStudent(id);
        return "redirect:/";
    }

    @GetMapping("/about")
    public String viewAboutPage() {
        return "about";
    }

    @GetMapping("/student/{id}")
    public String viewStudentDetails(@PathVariable(value = "id") String id, Model model) throws IOException {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);

        if (student != null && student.getEnrolledCourseIds() != null) {
            List<com.example.demo12.model.Course> enrolledCourses = new java.util.ArrayList<>();
            for (String courseId : student.getEnrolledCourseIds()) {
                com.example.demo12.model.Course course = courseService.getCourseById(courseId);
                if (course != null) {
                    enrolledCourses.add(course);
                }
            }
            model.addAttribute("enrolledCourses", enrolledCourses);
        }

        model.addAttribute("attendanceRecords", attendanceService.getAttendanceByStudentId(id));

        Map<String, String> courseNames = courseService.getAllCourses().stream()
                .collect(java.util.stream.Collectors.toMap(com.example.demo12.model.Course::getId, com.example.demo12.model.Course::getName));
        model.addAttribute("courseNames", courseNames);

        return "student_details";
    }

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) throws IOException {
        model.addAttribute("studentCount", studentService.getStudentCount());
        return "dashboard";
    }

    @GetMapping("/students/manageEnrollments/{id}")
    public String showEnrollmentForm(@PathVariable(value = "id") String id, Model model) throws IOException {
        Student student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("allCourses", courseService.getAllCourses());
        return "manage_enrollments";
    }

    @PostMapping("/students/updateEnrollments")
    public String updateEnrollments(@RequestParam("studentId") String studentId, @RequestParam(value = "courseIds", required = false) List<String> courseIds) throws IOException {
        Student student = studentService.getStudentById(studentId);
        if (student != null) {
            if (courseIds == null) {
                student.setEnrolledCourseIds(new java.util.ArrayList<>());
            } else {
                student.setEnrolledCourseIds(courseIds);
            }
            studentService.updateStudent(student);
        }
        return "redirect:/student/" + studentId;
    }
}
