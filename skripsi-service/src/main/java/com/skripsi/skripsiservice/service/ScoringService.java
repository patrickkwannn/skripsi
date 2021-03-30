package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.ScoringDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.CourseTaken;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.CourseTakenRepository;
import com.skripsi.skripsiservice.repository.UserTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoringService {

    @Autowired
    CourseTakenRepository courseTakenRepository;

    @Autowired
    UserTableRepository userTableRepository;

    @Autowired
    CourseRepository courseRepository;

    public void submitScore(ScoringDomain scoringDomain) throws RequestException {

        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(scoringDomain.getUserId(),"N");
        Course course = courseRepository.findCourseByIdAndIsDeleted(scoringDomain.getCourseID(),"N");
        CourseTaken courseTaken = courseTakenRepository.findCourseTakensByUserTableAndAndCourseAndIsDeleted(userTable,course,"N");

        if (courseTaken==null)
            throw new RequestException("user or course not found");

        courseTaken.setExamScore(scoringDomain.getScore());
        courseTaken.setIsDeleted("N");
        courseTakenRepository.save(courseTaken);

    }
    public List<CourseTaken> getAll(){
        return courseTakenRepository.findCourseTakenByIsDeleted("N");
    }

    public ScoringDomain getScore(String courseId, String userId){
        ScoringDomain scoringDomainResponse= new ScoringDomain();

        UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(userId,"N");
        Course course = courseRepository.findCourseByIdAndIsDeleted(courseId,"N");
        CourseTaken courseTaken = courseTakenRepository.findCourseTakensByUserTableAndAndCourseAndIsDeleted(userTable,course,"N");

        scoringDomainResponse.setCourseID(course.getId());
        scoringDomainResponse.setCourseName(course.getCourseName());
        scoringDomainResponse.setScore(courseTaken.getExamScore());
        scoringDomainResponse.setUserId(userTable.getUserId());
        return scoringDomainResponse;
    }
}
