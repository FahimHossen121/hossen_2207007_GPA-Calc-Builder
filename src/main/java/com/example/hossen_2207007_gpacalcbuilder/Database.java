package com.example.hossen_2207007_gpacalcbuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:gpa_calculator.db";

    // Initialize database and table
    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS courses (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "name TEXT NOT NULL," +
                        "code TEXT NOT NULL," +
                        "credit REAL NOT NULL," +
                        "grade TEXT NOT NULL," +
                        "teacher1 TEXT NOT NULL," +
                        "teacher2 TEXT NOT NULL" +
                        ");";
                stmt.execute(sql);
                System.out.println("Database initialized and table created.");
            }
        } catch (SQLException e) {
            System.out.println("Database initialization error: " + e.getMessage());
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    public static void insertCourse(CourseModel course) {
        String sql = "INSERT INTO courses(name, code, credit, grade, teacher1, teacher2) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set values for each placeholder
            pstmt.setString(1, course.getName());
            pstmt.setString(2, course.getCode());
            pstmt.setDouble(3, course.getCredit());
            pstmt.setString(4, course.getGrade());
            pstmt.setString(5, course.getTeacher1());
            pstmt.setString(6, course.getTeacher2());

            // Execute the insert
            pstmt.executeUpdate();
            System.out.println("Course inserted: " + course.getName());

        } catch (SQLException e) {
            System.out.println("Error inserting course: " + e.getMessage());
        }
    }
}