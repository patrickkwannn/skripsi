package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.CourseTakenDomain;
import com.skripsi.skripsiservice.domain.TakeCourseTransactionDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.CourseTaken;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.CourseTakenRepository;
import com.skripsi.skripsiservice.repository.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseTakenService {

    @Autowired
    CourseTakenRepository courseTakenRepository;

    @Autowired
    UserTableRepository userTableRepository;

    @Autowired
    CourseRepository courseRepository;

    public void takeCourse(TakeCourseTransactionDomain courseTakenDomain) throws RequestException {
        CourseTaken addCourse= new CourseTaken();

        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(courseTakenDomain.getUserId(),"N");
        Course course = courseRepository.findCourseByIdAndIsDeleted(courseTakenDomain.getCourseId(),"N");
        List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakensByUserTableAndIsDeleted(userTable,"N");
        for (CourseTaken courseTaken :  courseTakens){
            if (courseTaken.getCourse().equals(course)){
                throw new RequestException("Course already taken ");
            }
        }
        addCourse.setCourse(course);
        addCourse.setUserTable(userTable);
        addCourse.setIsDeleted("N");
        courseTakenRepository.save(addCourse);
    }
    public List<CourseTakenDomain> viewAllTakenCourse(String id) throws RequestException {
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(id,"N");
        if (userTable==null)
            throw new RequestException("user not found for id: "+id);
        List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakensByUserTableAndIsDeleted(userTable,"N");
        if (courseTakens==null || courseTakens.size()==0)
            throw new RequestException("no course can be viewed");

        List<CourseTakenDomain> courseTakenDomains = new ArrayList<>();
        for (CourseTaken courseTaken: courseTakens){
            CourseTakenDomain courseTakenDomain = new CourseTakenDomain();
            courseTakenDomain.setCourse(courseTaken.getCourse());
                courseTakenDomains.add(courseTakenDomain);
        }
       return courseTakenDomains;
    }

    public List<CourseTakenDomain> viewAllCourseAvailable(String id) throws RequestException {
        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(id,"N");
        List<CourseTakenDomain> courseTakenDomains = new ArrayList<>();
        List<Course> courses = courseRepository.findCoursesByIsDeleted("N");

        if (userTable==null )
            throw new RequestException("user not found for id: "+id);

        List<CourseTaken> courseTakens = courseTakenRepository.findCourseTakensByUserTableAndIsDeleted(userTable,"N");
        if (courseTakens.size()==0) {//blm ad course yang di ambl maka view semua
            for (Course course: courses){
                    CourseTakenDomain courseTakenDomain = new CourseTakenDomain();
                    courseTakenDomain.setCourse(course);
                    courseTakenDomains.add(courseTakenDomain);
            }
        }
        else {
            for (Course course : courses) {
               CourseTaken courseTaken = courseTakenRepository.findCourseTakensByUserTableAndAndCourseAndIsDeleted(userTable,course,"N");
               if (courseTaken==null || courseTaken.equals(null)){
                   CourseTakenDomain courseTakenDomain = new CourseTakenDomain();
                   courseTakenDomain.setCourse(course);
                   courseTakenDomains.add(courseTakenDomain);
               }
            }
        }
        return courseTakenDomains;
    }

    public void deleteCourse(String courseTakenId) throws RequestException {
        CourseTaken courseTaken = courseTakenRepository.findCourseTakenByIdAndIsDeleted(courseTakenId,"N");

        if (courseTaken==null)
            throw new RequestException("no course taken ");

        courseTaken.setIsDeleted("Y");
        courseTakenRepository.save(courseTaken);
    }
}
