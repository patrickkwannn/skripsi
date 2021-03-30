package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.CourseTaken;
import com.skripsi.skripsiservice.model.UserTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTakenRepository extends JpaRepository<CourseTaken,String> {

    List<CourseTaken> findCourseTakensByUserTableAndIsDeleted(UserTable userTable,String isDeleted);

    CourseTaken findCourseTakensByUserTableAndAndCourseAndIsDeleted(UserTable userTable, Course course,String isDeleted);

    CourseTaken findCourseTakenByIdAndIsDeleted(String id,String isDeleted);

    List<CourseTaken> findCourseTakenByIsDeleted(String isDeleted);
}
