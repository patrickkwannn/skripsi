package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.PostApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostApplication,String> {

    PostApplication findPostApplicationByPostApplicationIdAndIsDeleted(String id,String isDeleted);

    List<PostApplication> findPostApplicationByCourseAndIsDeleted(Course course,String isDeleted);
}
