package com.skripsi.skripsiservice.model;

import com.skripsi.skripsiservice.generator.StringSequenceIdentifier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "CommentApplication")
public class CommentApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comment_seq")
    @GenericGenerator(name="comment_seq",strategy = "com.skripsi.skripsiservice.generator.StringSequenceIdentifier",parameters = {
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.INCREMENT_PARAM, value = "50"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.VALUE_PREFIX_PARAMETER, value = "CMT"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.NUMBER_FORMAT_PARAMETER, value = "%05d")
    })
    @Column(name = "COMMENT_APPLICATION_ID",unique=true, nullable = false)
    private String id;

    @Column(name = "COMMENT_DESCRIPTION")
    private String commentDescription;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostApplication postApplication;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserTable userTable;

    @Column(name = "IS_DELETED")
    private String isDeleted;

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

    public String getCommentDescription() {
        return commentDescription;
    }

    public void setCommentDescription(String commentDescription) {
        this.commentDescription = commentDescription;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public PostApplication getPostApplication() {
        return postApplication;
    }

    public void setPostApplication(PostApplication postApplication) {
        this.postApplication = postApplication;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }
}
