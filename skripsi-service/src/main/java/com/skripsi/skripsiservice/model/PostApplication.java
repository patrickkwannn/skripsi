package com.skripsi.skripsiservice.model;

import com.skripsi.skripsiservice.generator.StringSequenceIdentifier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PostApplication")
public class PostApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "post_seq")
    @GenericGenerator(name="post_seq",strategy = "com.skripsi.skripsiservice.generator.StringSequenceIdentifier",parameters = {
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.INCREMENT_PARAM, value = "50"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.VALUE_PREFIX_PARAMETER, value = "PS"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.NUMBER_FORMAT_PARAMETER, value = "%05d")
    })
    @Column(name = "POST_ID",unique=true, nullable = false)
    private String postApplicationId;

    @Column(name = "POST_QUESTION")
    private String postQuestion;

    @Column(name = "POST_TOPIC")
    private String postTopic;

    @Column(name = "POST_DESCRIPTION")
    private String postDescription;

    @Column(name = "CREATED_DATE_TIME")
    private Date createdDateTime;

    @Column(name = "SOLVED_DATE_TIME")
    private Date solvedDateTime;

    @Column(name = "POST_STATUS")
    private String postStatus;

    @ManyToOne
    @JoinColumn(name = "COURSE_ID")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserTable userTable;

    @Column(name = "IS_DELETED")
    private String isDeleted;

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getPostApplicationId() {
        return postApplicationId;
    }

    public void setPostApplicationId(String id) {
        this.postApplicationId = id;
    }

    public String getPostQuestion() {
        return postQuestion;
    }

    public void setPostQuestion(String postQuestion) {
        this.postQuestion = postQuestion;
    }

    public String getPostTopic() {
        return postTopic;
    }

    public void setPostTopic(String postData) {
        this.postTopic = postData;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getSolvedDateTime() {
        return solvedDateTime;
    }

    public void setSolvedDateTime(Date solvedDateTime) {
        this.solvedDateTime = solvedDateTime;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postType) {
        this.postStatus = postType;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }


}
