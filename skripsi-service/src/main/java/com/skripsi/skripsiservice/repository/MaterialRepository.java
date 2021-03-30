package com.skripsi.skripsiservice.repository;

import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material,String> {
    List<Material> findMaterialsByCourseAndIsDeleted(Course course,String isDeleted);
    Material findMaterialByIdAndIsDeleted(String materialId,String isDeleted);
    List<Material> findMaterialsByIsDeleted(String isDeleted);
}
