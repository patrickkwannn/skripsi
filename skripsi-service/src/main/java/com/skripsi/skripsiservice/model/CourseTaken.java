package com.skripsi.skripsiservice.model;

import com.skripsi.skripsiservice.generator.StringSequenceIdentifier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "Course_Taken")
public class CourseTaken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "course_taken_seq")
    @GenericGenerator(name="course_taken_seq",strategy = "com.skripsi.skripsiservice.generator.StringSequenceIdentifier",parameters = {
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.INCREMENT_PARAM, value = "50"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.VALUE_PREFIX_PARAMETER, value = "CT"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.NUMBER_FORMAT_PARAMETER, value = "%05d")
    })
    @Column(name = "Course_Taken_Id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable userTable;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;

    @Column(name = "EXAM_SCORE")
    private Integer examScore;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getExamScore() {
        return examScore;
    }

    public void setExamScore(Integer examScore) {
        this.examScore = examScore;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
