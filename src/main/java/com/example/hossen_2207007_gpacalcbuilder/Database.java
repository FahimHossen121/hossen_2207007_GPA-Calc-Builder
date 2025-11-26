package com.example.hossen_2207007_gpacalcbuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {

    private static final String DB_URL = "jdbc:sqlite:gpa_calculator.db";

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

            pstmt.setString(1, course.getName());
            pstmt.setString(2, course.getCode());
            pstmt.setDouble(3, course.getCredit());
            pstmt.setString(4, course.getGrade());
            pstmt.setString(5, course.getTeacher1());
            pstmt.setString(6, course.getTeacher2());

            pstmt.executeUpdate();
            System.out.println("Course inserted: " + course.getName());

        } catch (SQLException e) {
            System.out.println("Error inserting course: " + e.getMessage());
        }
    }

    public static void clearAllCourses() {
        String sql = "DELETE FROM courses";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("All courses cleared from database.");

        } catch (SQLException e) {
            System.out.println("Error clearing courses: " + e.getMessage());
        }
    }

    public static ArrayList<CourseModel> getAllCourses() {
        ArrayList<CourseModel> courseList = new ArrayList<>();
        String sql = "SELECT * FROM courses";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String name = rs.getString("name");
                String code = rs.getString("code");
                double credit = rs.getDouble("credit");
                String grade = rs.getString("grade");
                String teacher1 = rs.getString("teacher1");
                String teacher2 = rs.getString("teacher2");

                CourseModel course = new CourseModel(name, code, credit, teacher1, teacher2, grade);
                courseList.add(course);
            }

            System.out.println("Fetched " + courseList.size() + " courses from database.");

        } catch (SQLException e) {
            System.out.println("Error fetching courses: " + e.getMessage());
        }

        return courseList;
    }

    public static void deleteCourse(String code) {
        String sql = "DELETE FROM courses WHERE code = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Course deleted: " + code);
            } else {
                System.out.println("No course found with code: " + code);
            }

        } catch (SQLException e) {
            System.out.println("Error deleting course: " + e.getMessage());
        }
    }
}
