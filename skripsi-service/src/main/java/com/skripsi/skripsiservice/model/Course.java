package com.skripsi.skripsiservice.model;

import com.skripsi.skripsiservice.generator.StringSequenceIdentifier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "course_seq")
    @GenericGenerator(name="course_seq",strategy = "com.skripsi.skripsiservice.generator.StringSequenceIdentifier",parameters = {
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.INCREMENT_PARAM, value = "50"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.VALUE_PREFIX_PARAMETER, value = "CO"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.NUMBER_FORMAT_PARAMETER, value = "%05d")
    })
    @Column(name = "COURSE_ID",unique=true, nullable = false)
    private String id;

    @Column(name = "COURSE_DESCRIPTION")
    private String courseDescription;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "COURSE_NAME")
    private String courseName;

    @Column(name = "COURSE_LO_1")
    private String courseLO1;

    @Column(name = "COURSE_LO_2")
    private String courseLO2;

    @Column(name = "COURSE_LO_3")
    private String courseLO3;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    @Column(name = "REFERENCE")
    private String reference;


    public String getCourseLO1() {
        return courseLO1;
    }

    public void setCourseLO1(String courseLO1) {
        this.courseLO1 = courseLO1;
    }

    public String getCourseLO2() {
        return courseLO2;
    }

    public void setCourseLO2(String courseLO2) {
        this.courseLO2 = courseLO2;
    }

    public String getCourseLO3() {
        return courseLO3;
    }

    public void setCourseLO3(String courseLO3) {
        this.courseLO3 = courseLO3;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String commentDescription) {
        this.courseDescription = commentDescription;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }


}
