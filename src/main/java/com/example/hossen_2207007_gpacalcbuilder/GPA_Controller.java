package com.example.hossen_2207007_gpacalcbuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class GPA_Controller {

    @FXML
    private Label resultLabel;

    @FXML
    private TableView<CourseModel> resultTable;

    @FXML
    private TableColumn<CourseModel, String> courseNameCol;

    @FXML
    private TableColumn<CourseModel, String> courseCodeCol;

    @FXML
    private TableColumn<CourseModel, Double> courseCreditCol;

    @FXML
    private TableColumn<CourseModel, String> gradeCol;

    @FXML
    private TableColumn<CourseModel, String> teacher1Col;

    @FXML
    private TableColumn<CourseModel, String> teacher2Col;

    @FXML
    public void initialize() {
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        courseCreditCol.setCellValueFactory(new PropertyValueFactory<>("credit"));
        gradeCol.setCellValueFactory(new PropertyValueFactory<>("grade"));
        teacher1Col.setCellValueFactory(new PropertyValueFactory<>("teacher1"));
        teacher2Col.setCellValueFactory(new PropertyValueFactory<>("teacher2"));

        ArrayList<CourseModel> coursesFromDB = Database.getAllCourses();

        ObservableList<CourseModel> courseList = FXCollections.observableArrayList(coursesFromDB);
        resultTable.setItems(courseList);

        if (!coursesFromDB.isEmpty()) {
            double totalCredits = 0;
            double weightedSum = 0;

            for (CourseModel course : coursesFromDB) {
                totalCredits += course.getCredit();
                weightedSum += course.getCredit() * gradeToPoint(course.getGrade());
            }

            double gpa = weightedSum / totalCredits;
            resultLabel.setText("Your GPA: " + String.format("%.2f", gpa));
            System.out.println("Calculated GPA: " + String.format("%.2f", gpa));
        } else {
            resultLabel.setText("No courses added!");
        }
    }

    @FXML
    public void deleteSelectedCourse() {
        CourseModel selectedCourse = resultTable.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to delete!");
            alert.showAndWait();
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete: " + selectedCourse.getName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            Database.deleteCourse(selectedCourse.getCode());

            Input_Controller.courses.remove(selectedCourse);

            refreshTable();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Course deleted successfully!");
            successAlert.showAndWait();
        }
    }

    private void refreshTable() {
        ArrayList<CourseModel> coursesFromDB = Database.getAllCourses();
        ObservableList<CourseModel> courseList = FXCollections.observableArrayList(coursesFromDB);
        resultTable.setItems(courseList);

        if (!coursesFromDB.isEmpty()) {
            double totalCredits = 0;
            double weightedSum = 0;

            for (CourseModel course : coursesFromDB) {
                totalCredits += course.getCredit();
                weightedSum += course.getCredit() * gradeToPoint(course.getGrade());
            }

            double gpa = weightedSum / totalCredits;
            resultLabel.setText("Your GPA: " + String.format("%.2f", gpa));
        } else {
            resultLabel.setText("No courses added!");
        }
    }

    @FXML
    public void backHome() {
        try {
            Stage stage = (Stage) resultLabel.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml"))));
            stage.setScene(scene);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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
            default -> 0.0;
        };
    }
}