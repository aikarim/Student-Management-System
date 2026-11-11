# Student Management System

A desktop-based Java application built using Java Swing and AWT. This system allows administrators to efficiently maintain and manage student information. The data is persistently stored using Java Object Serialization (file handling).

## Features

*   **Admin Authentication:** Secure login screen for administrators.
*   **Add Student:** Enter and save new student records.
*   **Update Student:** Modify existing student details.
*   **Delete Student:** Remove student records from the system.
*   **Search Student:** Quickly find a student using their unique Student ID.
*   **Persistent Storage:** All data is saved to a local `students.dat` file, ensuring information is not lost between sessions.
*   **Object-Oriented Design:** Implements classes, inheritance, encapsulation, and collections.

## Technologies Used

*   **Language:** Java
*   **GUI Framework:** Java Swing & AWT
*   **Data Storage:** File Handling (Java Object Serialization)

## Admin Login Credentials

To access the main dashboard of the application, use the following credentials on the login screen:

*   **Username:** `admin`
*   **Password:** `admin123`

## How to Run the Project

1. Ensure you have the Java Development Kit (JDK) installed on your system.
2. Download or clone the `StudentManagementSystem.java` file to your local machine.
3. Open a terminal or command prompt and navigate to the directory where the file is saved.
4. Compile the Java file using the following command:
   ```bash
   javac StudentManagementSystem.java
   ```
5. Run the compiled application using the following command:
   ```bash
   java StudentManagementSystem
   ```
6. Enter the admin credentials provided above to log in and manage students.
