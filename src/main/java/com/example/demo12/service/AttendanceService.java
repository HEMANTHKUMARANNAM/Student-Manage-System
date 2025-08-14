package com.example.demo12.service;

import com.example.demo12.model.AttendanceRecord;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final File jsonFile = new File("src/main/resources/attendance.json");

    public List<AttendanceRecord> getAllAttendanceRecords() throws IOException {
        if (!jsonFile.exists() || jsonFile.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(jsonFile, new TypeReference<List<AttendanceRecord>>() {});
    }

    public void saveAttendanceRecords(List<AttendanceRecord> records) throws IOException {
        List<AttendanceRecord> existingRecords = getAllAttendanceRecords();
        // This simple approach replaces all records for a given day and course, which is fine for this use case.
        existingRecords.removeIf(r -> r.getDate().equals(records.get(0).getDate()) && r.getCourseId().equals(records.get(0).getCourseId()));
        existingRecords.addAll(records);
        objectMapper.writeValue(jsonFile, existingRecords);
    }

    public List<AttendanceRecord> getAttendanceByStudentId(String studentId) throws IOException {
        return getAllAttendanceRecords().stream()
                .filter(r -> r.getStudentId().equals(studentId))
                .collect(Collectors.toList());
    }

    public List<AttendanceRecord> getAttendanceByCourseId(String courseId) throws IOException {
        return getAllAttendanceRecords().stream()
                .filter(r -> r.getCourseId().equals(courseId))
                .collect(Collectors.toList());
    }
}
