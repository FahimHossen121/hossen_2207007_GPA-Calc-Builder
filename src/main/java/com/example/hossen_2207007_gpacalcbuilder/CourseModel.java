package com.example.hossen_2207007_gpacalcbuilder;

public class CourseModel {
    private String name;
    private String code;
    private double credit;
    private String teacher1;
    private String teacher2;
    private String grade;

    public CourseModel(String name, String code, double credit, String teacher1, String teacher2, String grade) {
        this.name = name;
        this.code = code;
        this.credit = credit;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
        this.grade = grade;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public double getCredit() {
        return credit;
    }

    public String getTeacher1() {
        return teacher1;
    }

    public String getTeacher2() {
        return teacher2;
    }

    public String getGrade() {
        return grade;
    }

}
