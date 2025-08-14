package com.example.demo12.service;

import com.example.demo12.model.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File jsonFile = new File("src/main/resources/students.json");

    public String saveStudent(Student student) throws IOException {
        List<Student> students = getAllStudents();
        students.add(student);
        objectMapper.writeValue(jsonFile, students);
        return "Student saved successfully";
    }

    public Student getStudentById(String id) throws IOException {
        return getAllStudents().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Student> getAllStudents() throws IOException {
        if (!jsonFile.exists() || jsonFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(jsonFile, new TypeReference<List<Student>>() {});
    }

    public String updateStudent(Student student) throws IOException {
        List<Student> students = getAllStudents();
        List<Student> updatedStudents = students.stream()
                .map(s -> s.getId().equals(student.getId()) ? student : s)
                .collect(Collectors.toList());
        objectMapper.writeValue(jsonFile, updatedStudents);
        return "Student updated successfully";
    }

    public String deleteStudent(String id) throws IOException {
        List<Student> students = getAllStudents();
        students.removeIf(s -> s.getId().equals(id));
        objectMapper.writeValue(jsonFile, students);
        return "Student with ID " + id + " has been deleted successfully";
    }

    public List<Student> searchStudents(String keyword) throws IOException {
        return getAllStudents().stream()
                .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public int getStudentCount() throws IOException {
        return getAllStudents().size();
    }
}

