package com.example.hossen_2207007_gpacalcbuilder;

import com.example.hossen_2207007_gpacalcbuilder.CourseModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Input_Controller {

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeBox;
    @FXML private Button calcBtn;

    public static ArrayList<CourseModel> courses = new ArrayList<>();
    private static final double TOTAL_REQUIRED_CREDIT = 25.0;

    @FXML
    public void initialize() {
        gradeBox.getItems().addAll("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F");
        calcBtn.setDisable(true);
    }
    @FXML
    public void addCourse() {
        try {
            // Get input values
            String cname = courseNameField.getText().trim();
            String ccode = courseCodeField.getText().trim();
            String creditText = courseCreditField.getText().trim();
            String t1 = teacher1Field.getText().trim();
            String t2 = teacher2Field.getText().trim();
            String grade = gradeBox.getValue();

            // Validate all fields are filled
            if (cname.isEmpty() || ccode.isEmpty() || creditText.isEmpty() ||
                    t1.isEmpty() || t2.isEmpty() || grade == null) {
                showAlert("Please fill all fields!");
                return;
            }

            // Parse credit
            double credit = Double.parseDouble(creditText);

            // Validate credit is positive
            if (credit <= 0) {
                showAlert("Credit must be a positive.");
                return;
            }

            // Add course to list
            courses.add(new CourseModel(cname, ccode, credit, t1, t2, grade));
            showAlert("Course added successfully!");

            // Check if enough credits have been added
            double totalCredits = courses.stream().mapToDouble(CourseModel::getCredit).sum();
            if (totalCredits >= TOTAL_REQUIRED_CREDIT) {
                calcBtn.setDisable(false);
            }

            // Clear all input fields
            clearFields();

        } catch (NumberFormatException e) {
            showAlert("Please enter a valid number for course credit!");
        } catch (Exception e) {
            showAlert("An error occurred: " + e.getMessage());
        }
    }

    private void clearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeBox.getSelectionModel().clearSelection();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void calculateGPA(ActionEvent actionEvent) throws IOException {
        System.out.println("Total Courses Added: " + courses.size());
        System.out.println();

        for (int i = 0; i < courses.size(); i++) {
            CourseModel course = courses.get(i);

            System.out.println("Course " + (i + 1) + ":");
            System.out.println("  Course Name: " + course.getName());
            System.out.println("  Course Code: " + course.getCode());
            System.out.println("  Course Credit: " + course.getCredit());
            System.out.println("  Teacher 1: " + course.getTeacher1());
            System.out.println("  Teacher 2: " + course.getTeacher2());
            System.out.println("  Grade: " + course.getGrade());
            System.out.println();
        }
    }
}