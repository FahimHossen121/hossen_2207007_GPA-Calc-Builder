package com.example.hossen_2207007_gpacalcbuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
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
    public void addNewCourse() {
        try {
            Stage stage = (Stage) resultLabel.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Input_view.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setMaximized(true);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error opening input form: " + e.getMessage());
            alert.showAndWait();
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

    @FXML
    public void editSelectedCourse() {
        CourseModel selectedCourse = resultTable.getSelectionModel().getSelectedItem();

        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a course to edit!");
            alert.showAndWait();
            return;
        }

        showEditDialog(selectedCourse);
    }

    private void showEditDialog(CourseModel course) {
        Dialog<CourseModel> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");
        dialog.setHeaderText("Edit course details");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField(course.getName());
        TextField codeField = new TextField(course.getCode());
        TextField creditField = new TextField(String.valueOf(course.getCredit()));
        TextField teacher1Field = new TextField(course.getTeacher1());
        TextField teacher2Field = new TextField(course.getTeacher2());
        ComboBox<String> gradeBox = new ComboBox<>();
        gradeBox.getItems().addAll("A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F");
        gradeBox.setValue(course.getGrade());

        grid.add(new Label("Course Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Course Code:"), 0, 1);
        grid.add(codeField, 1, 1);
        grid.add(new Label("Credit:"), 0, 2);
        grid.add(creditField, 1, 2);
        grid.add(new Label("Grade:"), 0, 3);
        grid.add(gradeBox, 1, 3);
        grid.add(new Label("Teacher 1:"), 0, 4);
        grid.add(teacher1Field, 1, 4);
        grid.add(new Label("Teacher 2:"), 0, 5);
        grid.add(teacher2Field, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = nameField.getText().trim();
                    String code = codeField.getText().trim();
                    double credit = Double.parseDouble(creditField.getText().trim());
                    String grade = gradeBox.getValue();
                    String teacher1 = teacher1Field.getText().trim();
                    String teacher2 = teacher2Field.getText().trim();

                    if (name.isEmpty() || code.isEmpty() || grade == null || teacher1.isEmpty() || teacher2.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("All fields are required!");
                        alert.showAndWait();
                        return null;
                    }

                    return new CourseModel(name, code, credit, teacher1, teacher2, grade);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid credit value!");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(updatedCourse -> {
            String oldCode = course.getCode();
            Database.updateCourse(oldCode, updatedCourse);

            for (int i = 0; i < Input_Controller.courses.size(); i++) {
                if (Input_Controller.courses.get(i).getCode().equals(oldCode)) {
                    Input_Controller.courses.set(i, updatedCourse);
                    break;
                }
            }

            refreshTable();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Course updated successfully!");
            successAlert.showAndWait();
        });
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