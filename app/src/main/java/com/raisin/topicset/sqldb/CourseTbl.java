package com.raisin.topicset.sqldb;

public class CourseTbl {
    private int id;
    // 1: course; 2: semester
    private String type = "";
    private String courseName = "";
    private String gradeId = "";
    private String gradeName = "";
    private String semesterId = "";
    private String semesterName = "";

    public CourseTbl() {
    }

    public CourseTbl(String type, String courseName, String gradeId, String gradeName, String semesterId, String semesterName) {
        this.type = type;
        this.courseName = courseName;
        this.gradeId = gradeId;
        this.gradeName = gradeName;
        this.semesterId = semesterId;
        this.semesterName = semesterName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }
}
