package com.skripsi.skripsiservice.domain;

import com.skripsi.skripsiservice.model.UserTable;

public class CourseDomain {

    private String courseId;

    private String courseDescription;

    private String courseName;

    private String lo1;

    private String lo2;

    private String lo3;

    private UserTable userTable;

    private int totalMaterial;

    private int totalStudyCase;

    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getLo1() {
        return lo1;
    }

    public void setLo1(String lo1) {
        this.lo1 = lo1;
    }

    public String getLo2() {
        return lo2;
    }

    public void setLo2(String lo2) {
        this.lo2 = lo2;
    }

    public String getLo3() {
        return lo3;
    }

    public void setLo3(String lo3) {
        this.lo3 = lo3;
    }

    public int getTotalMaterial() {
        return totalMaterial;
    }

    public void setTotalMaterial(int totalMaterial) {
        this.totalMaterial = totalMaterial;
    }

    public int getTotalStudyCase() {
        return totalStudyCase;
    }

    public void setTotalStudyCase(int totalStudyCase) {
        this.totalStudyCase = totalStudyCase;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }
}
