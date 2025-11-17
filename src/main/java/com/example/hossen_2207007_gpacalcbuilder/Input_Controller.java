package com.example.hossen_2207007_gpacalcbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        // Only need to load grades
        gradeBox.getItems().addAll("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F");
        calcBtn.setDisable(true);
    }

    @FXML
    public void addCourse() {
        // Directly read values (since all are valid)
        String cname = courseNameField.getText().trim();
        String ccode = courseCodeField.getText().trim();
        double credit = Double.parseDouble(courseCreditField.getText().trim());
        String t1 = teacher1Field.getText().trim();
        String t2 = teacher2Field.getText().trim();
        String grade = gradeBox.getValue();

        // Add course (no validation now)
        courses.add(new CourseModel(cname, ccode, credit, t1, t2, grade));

        // Enable Calculate button when credits reach requirement
        double totalCredits = courses.stream().mapToDouble(CourseModel::getCredit).sum();
        if (totalCredits >= TOTAL_REQUIRED_CREDIT) {
            calcBtn.setDisable(false);
        }

        // Clear fields
        clearFields();
    }

    private void clearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void calculateGPA(ActionEvent actionEvent) {
        System.out.println("Total Courses Added: " + courses.size());
        System.out.println();

        for (int i = 0; i < courses.size(); i++) {
            CourseModel c = courses.get(i);

            System.out.println("Course " + (i + 1) + ":");
            System.out.println("  Name: " + c.getName());
            System.out.println("  Code: " + c.getCode());
            System.out.println("  Credit: " + c.getCredit());
            System.out.println("  Teacher 1: " + c.getTeacher1());
            System.out.println("  Teacher 2: " + c.getTeacher2());
            System.out.println("  Grade: " + c.getGrade());
            System.out.println();
        }
    }
}
