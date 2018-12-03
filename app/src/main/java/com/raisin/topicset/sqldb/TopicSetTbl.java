package com.raisin.topicset.sqldb;

public class TopicSetTbl {
    private int id;
    private String courseId;
    private String gradeId = "";
    private String semesterId = "";
    private String problemNm = ""; // 一个以上用,分割
    private String answerNm = ""; // 一个以上用,分割
    private String path = ""; // 存放jpg的目录

    public TopicSetTbl() {
    }

    public TopicSetTbl(String courseId, String gradeId, String semesterId, String problemNm, String answerNm, String path) {
        this.courseId = courseId;
        this.gradeId = gradeId;
        this.semesterId = semesterId;
        this.problemNm = problemNm;
        this.answerNm = answerNm;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getProblemNm() {
        return problemNm;
    }

    public void setProblemNm(String problemNm) {
        this.problemNm = problemNm;
    }

    public String getAnswerNm() {
        return answerNm;
    }

    public void setAnswerNm(String answerNm) {
        this.answerNm = answerNm;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
