package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.MaterialDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.CourseTaken;
import com.skripsi.skripsiservice.model.Material;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.CourseTakenRepository;
import com.skripsi.skripsiservice.repository.MaterialRepository;
import com.skripsi.skripsiservice.repository.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MaterialService {

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseTakenRepository courseTakenRepository;

    @Autowired
    UserTableRepository userTableRepository;

    public void addMaterial(MaterialDomain materialDomain) throws RequestException {
        Course course = courseRepository.findCourseByIdAndIsDeleted(materialDomain.getCourseId(),"N");
        Material material = new Material();
        List<Material> materials = materialRepository.findMaterialsByCourseAndIsDeleted(course,"N");
        if (course==null)
            throw new RequestException("course not found for id"+materialDomain.getCourseId());


        material.setMaterialDescription(materialDomain.getMaterialDescription());
        material.setMaterialLink(materialDomain.getMaterialUrl());
        material.setMaterialName(materialDomain.getMaterialName());
        material.setMaterialType(materialDomain.getMaterialType());
        material.setCourse(course);

        if (materials!=null){
            for (Material material1 : materials){
                if (material1.getMaterialLevel().equals(materialDomain.getMaterialLevel()))
                    throw new RequestException("Material Level exist");
            }
        }
        material.setIsDeleted("N");
        material.setMaterialLevel(materialDomain.getMaterialLevel());
        materialRepository.save(material);
    }
    public List<Material> viewAllMaterial(){
        return materialRepository.findMaterialsByIsDeleted("N");
    }

    public List<Material> getMaterialQuiz(String userId){
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(userId,"N");
        List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakensByUserTableAndIsDeleted(userTable,"N");
        List<Material> materialResponse = new ArrayList<>();
            for (CourseTaken courseTaken : courseTakens ){
                System.out.println("masuk");
                Course course = courseRepository.findCourseByIdAndIsDeleted(courseTaken.getCourse().getId(),"N");
                List<Material> materials = materialRepository.findMaterialsByCourseAndIsDeleted(course,"N");
                System.out.println("test"+materials.size());
                for (Material material : materials){
                    if (material.getMaterialType().equals("QUIZ")){
                         materialResponse.add(material);
                     }
                }
            }
            return materialResponse;
    }

    public void deleteMaterial(String materialId) throws RequestException {

        Material material = materialRepository.findMaterialByIdAndIsDeleted(materialId,"N");

        if (material==null)
            throw new RequestException("material doesnt exist");

        material.setIsDeleted("Y");
        materialRepository.save(material);
    }
}
