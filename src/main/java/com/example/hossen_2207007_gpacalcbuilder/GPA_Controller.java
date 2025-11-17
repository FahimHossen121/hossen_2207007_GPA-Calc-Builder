package com.example.hossen_2207007_gpacalcbuilder;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;

public class GPA_Controller {

    @FXML
    private Label resultLabel;

    @FXML
    public void initialize() {
        // Calculate GPA when this view is loaded
        if (Input_Controller.courses.isEmpty()) {
            resultLabel.setText("No courses added!");
            return;
        }

        double totalCredits = 0;
        double weightedSum = 0;

        for (CourseModel course : Input_Controller.courses) {
            totalCredits += course.getCredit();
            weightedSum += course.getCredit() * gradeToPoint(course.getGrade());
        }

        double gpa = weightedSum / totalCredits;
        resultLabel.setText("Your GPA: " + String.format("%.2f", gpa));

    }

    @FXML
    public void backHome() {
        try {
            // Clear courses if needed
            // Input_Controller.courses.clear();

            Stage stage = (Stage) resultLabel.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Input_View.fxml"))));
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Helper method to convert grade to points
    private double gradeToPoint(String g) {
        return switch (g) {
            case "A+" -> 4.0;
            case "A" -> 3.75;
            case "A-" -> 3.50;
            case "B+" -> 3.25;
            case "B" -> 3.00;
            case "B-" -> 2.75;
            case "C+" -> 2.50;
            case "C" -> 2.25;
            case "D" -> 2.00;
            default -> 0.0; // F or invalid
        };
    }
}
