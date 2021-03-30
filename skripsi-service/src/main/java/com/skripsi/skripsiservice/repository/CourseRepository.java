package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course,String> {

    Course findCourseByIdAndIsDeleted(String id,String isDeleted);

    List<Course> findCoursesByIsDeleted(String isDeleted);

}
