package com.example.hossen_2207007_gpacalcbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
            String cname = courseNameField.getText().trim();
            String ccode = courseCodeField.getText().trim();
            String creditText = courseCreditField.getText().trim();
            String t1 = teacher1Field.getText().trim();
            String t2 = teacher2Field.getText().trim();
            String grade = gradeBox.getValue();

            if (cname.isEmpty() || ccode.isEmpty() || creditText.isEmpty() ||
                    t1.isEmpty() || t2.isEmpty() || grade == null) {
                showAlert("Please fill all fields!");
                return;
            }

            double credit = Double.parseDouble(creditText);
            if (credit <= 0) {
                showAlert("Credit must be a positive number.");
                return;
            }

            courses.add(new CourseModel(cname, ccode, credit, t1, t2, grade));
            showAlert("Course added successfully!");

            // Enable Calculate button if total credits >= required
            double totalCredits = courses.stream().mapToDouble(CourseModel::getCredit).sum();
            if (totalCredits >= TOTAL_REQUIRED_CREDIT) {
                calcBtn.setDisable(false);
            }

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
    public void calculateGPA(ActionEvent actionEvent) {
        if (courses.isEmpty()) {
            showAlert("No courses added yet!");
            return;
        }
        try {
            Stage stage = (Stage) calcBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GPA_Calculator_view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (Exception e) {
            showAlert("Error opening GPA result screen: " + e.getMessage());
        }
    }
}
