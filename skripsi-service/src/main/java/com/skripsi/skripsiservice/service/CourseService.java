package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.*;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.Material;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MaterialRepository materialRepository;

    public void addCourse(CourseDomain courseDomain){
        Course newCourse = new Course();
        newCourse.setCourseName(courseDomain.getCourseName());
        newCourse.setCourseDescription(courseDomain.getCourseDescription());
        newCourse.setCourseLO1(courseDomain.getLo1());
        if (courseDomain.getLo2()!=null)
            newCourse.setCourseLO2(courseDomain.getLo2());
        if (courseDomain.getLo3()!=null)
            newCourse.setCourseLO3(courseDomain.getLo3());
        newCourse.setReference(courseDomain.getReference());
        newCourse.setCreatedDate(new Date());
        newCourse.setIsDeleted("N");
        courseRepository.save(newCourse);
    }

    public void deleteCourse(String courseId) throws RequestException {
        Course course = courseRepository.findCourseByIdAndIsDeleted(courseId,"N");
        if (course==null)
            throw new RequestException("course not found");

        course.setIsDeleted("Y");
        courseRepository.save(course);
    }
    public CourseDomain viewAvailableCourseDetail(String id) throws RequestException {
        Course course = courseRepository.findCourseByIdAndIsDeleted(id,"N");
        List<Material> materials = materialRepository.findMaterialsByCourseAndIsDeleted(course,"N");
        if (course==null)
            throw new RequestException("Course not found for id:"+id);
        int materialTotal=0;
        int materialStudyCaseTotal=0;
        CourseDomain courseDomain = new CourseDomain();
        courseDomain.setCourseDescription(course.getCourseDescription());
        courseDomain.setCourseName(course.getCourseName());
        courseDomain.setCourseId(course.getId());
        for (Material material :materials){
            if (material.getMaterialType().equals("MATERIAL"))
                materialTotal+=1;
            else
                materialStudyCaseTotal+=1;
        }
        courseDomain.setTotalMaterial(materialTotal);
        courseDomain.setTotalStudyCase(materialStudyCaseTotal);
        return courseDomain;
    }
    public ViewMaterialCourseDomain viewTakenCourseDetail(String id) throws RequestException {
        Course course = courseRepository.findCourseByIdAndIsDeleted(id,"N");
        List<String> learningOutcome= new ArrayList<>();
        List<Material> materials = materialRepository.findMaterialsByCourseAndIsDeleted(course,"N");
        if (course==null)
            throw new RequestException("Course not found for id:"+id);
        ViewMaterialCourseDomain viewMaterialCourseDomain = new ViewMaterialCourseDomain();
        viewMaterialCourseDomain.setCourseName(course.getCourseName());
        List<MaterialDomain> materialDomains = new ArrayList<>();
        for (Material material : materials){
            MaterialDomain materialDomain = new MaterialDomain();
            materialDomain.setMaterialId(material.getId());
            materialDomain.setMaterialName(material.getMaterialName());
            materialDomain.setMaterialDescription(material.getMaterialDescription());
            materialDomain.setMaterialType(material.getMaterialType());
            materialDomain.setMaterialUrl(material.getMaterialLink());
            materialDomain.setMaterialLevel(material.getMaterialLevel());
            materialDomain.setCourseId(course.getId());
            materialDomains.add(materialDomain);
        }
        viewMaterialCourseDomain.setReference(course.getReference());
        if (course.getCourseLO1()!=null)
            learningOutcome.add(course.getCourseLO1());
        if (course.getCourseLO2()!=null)
            learningOutcome.add(course.getCourseLO2());
        if (course.getCourseLO3()!=null)
            learningOutcome.add(course.getCourseLO3());
        viewMaterialCourseDomain.setLearningOutcomes(learningOutcome);
        viewMaterialCourseDomain.setCourseDescription(course.getCourseDescription());
        viewMaterialCourseDomain.setMaterials(materialDomains);
        return viewMaterialCourseDomain;
    }
    public List<Course> viewAllCourse(){
        return courseRepository.findCoursesByIsDeleted("N");
    }

}
