package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.CommentApplication;
import com.skripsi.skripsiservice.model.PostApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentApplication,String> {
    List<CommentApplication> findCommentApplicationsByPostApplication(PostApplication postApplication);
}
