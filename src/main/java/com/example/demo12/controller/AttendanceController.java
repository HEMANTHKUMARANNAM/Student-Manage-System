package com.example.demo12.controller;

import com.example.demo12.model.AttendanceRecord;
import com.example.demo12.model.Course;
import com.example.demo12.model.Student;
import com.example.demo12.service.AttendanceService;
import com.example.demo12.service.CourseService;
import com.example.demo12.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/new")
    public String showSelectCourseForAttendance(Model model) throws IOException {
        model.addAttribute("courses", courseService.getAllCourses());
        return "select_course_for_attendance";
    }

    @PostMapping("/sheet")
    public String showAttendanceSheet(@RequestParam("courseId") String courseId, @RequestParam("date") String date, Model model) throws IOException {
        Course course = courseService.getCourseById(courseId);
        List<Student> allStudents = studentService.getAllStudents();

        List<Student> enrolledStudents = allStudents.stream()
                .filter(s -> s.getEnrolledCourseIds().contains(courseId))
                .collect(Collectors.toList());

        model.addAttribute("course", course);
        model.addAttribute("students", enrolledStudents);
        model.addAttribute("date", date);
        return "attendance_sheet";
    }

    @PostMapping("/save")
    public String saveAttendance(@RequestParam("courseId") String courseId, @RequestParam("date") String date, @RequestParam("studentIds") List<String> studentIds, @RequestParam("statuses") List<String> statuses) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        List<AttendanceRecord> records = new java.util.ArrayList<>();
        for (int i = 0; i < studentIds.size(); i++) {
            records.add(new AttendanceRecord(studentIds.get(i), courseId, localDate, statuses.get(i)));
        }
        attendanceService.saveAttendanceRecords(records);
        return "redirect:/courses"; // Or a more appropriate confirmation page
    }
}
