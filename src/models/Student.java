
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Student {
    public final int studentId, userId, sapId, yearOfStudy, maxAllowedBooks, currentBorrowedBooks;
    public final String department, program;

    public Student(int studentId, int userId, int sapId, String department, String program, int yearOfStudy,
                   int maxAllowedBooks, int currentBorrowedBooks) {
        this.studentId = studentId;
        this.userId = userId;
        this.sapId = sapId;
        this.department = department;
        this.program = program;
        this.yearOfStudy = yearOfStudy;
        this.maxAllowedBooks = maxAllowedBooks;
        this.currentBorrowedBooks = currentBorrowedBooks;
    }

    public static ArrayList<Student> getStudentsFromResultSet(ResultSet rs) {
        ArrayList<Student> students = new ArrayList<>();
        try {
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getInt("sap_id"),
                    rs.getString("department"),
                    rs.getString("program"),
                    rs.getInt("year_of_study"),
                    rs.getInt("max_allowed_books"),
                    rs.getInt("current_borrowed_books")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing students");
        }
        return students;
    }

    public static Optional<Student> getFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                return Optional.of(new Student(
                    rs.getInt("student_id"),
                    rs.getInt("user_id"),
                    rs.getInt("sap_id"),
                    rs.getString("department"),
                    rs.getString("program"),
                    rs.getInt("year_of_study"),
                    rs.getInt("max_allowed_books"),
                    rs.getInt("current_borrowed_books")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error parsing student");
        }
        return Optional.empty();
    }
}
