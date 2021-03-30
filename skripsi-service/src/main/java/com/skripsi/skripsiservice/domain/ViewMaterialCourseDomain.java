package com.skripsi.skripsiservice.domain;

import com.skripsi.skripsiservice.model.Material;

import java.util.List;

public class ViewMaterialCourseDomain {

    private String courseName;

    private String courseDescription;

    private List<MaterialDomain> materials;

    private List<String> learningOutcomes;

    private String reference;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public List<String> getLearningOutcomes() {
        return learningOutcomes;
    }

    public void setLearningOutcomes(List<String> learningOutcomes) {
        this.learningOutcomes = learningOutcomes;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<MaterialDomain> getMaterials() {
        return materials;
    }

    public void setMaterials(List<MaterialDomain> materials) {
        this.materials = materials;
    }
}
