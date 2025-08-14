# Student Management System

This is a simple Student Management System built with Spring Boot. It allows you to manage students, courses, and attendance.

## Features

*   **Student Management:** Add, edit, delete, and view student information.
*   **Course Management:** Create and manage courses.
*   **Attendance Tracking:** Record and view student attendance.
*   **Course Enrollment:** Enroll students in different courses.
*   **Search:** Search for students by name.

## Technologies Used

*   Java
*   Spring Boot
*   Thymeleaf
*   Bootstrap
*   jQuery
*   Maven

## Setup and Installation

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 21 or later.
    *   Apache Maven.

2.  **Clone the repository:**
    ```bash
    git clone https://github.com/HEMANTHKUMARANNAM/Student-Manage-System.git
    cd Student-Manage-System
    ```

3.  **Build the project:**
    ```bash
    mvn clean install
    ```

4.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

The application will be accessible at `http://localhost:8080`.

## Usage

*   The home page (`/`) displays a list of all students.
*   You can add a new student using the "Add Student" button.
*   You can edit or delete a student from the list.
*   Click on a student's name to view their details, including enrolled courses and attendance.
*   The dashboard (`/dashboard`) shows the total number of students.
*   You can manage course enrollments for each student.
