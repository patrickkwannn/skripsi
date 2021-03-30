package com.skripsi.skripsiservice.service;

import com.skripsi.skripsiservice.domain.MaterialTypeCheck;
import com.skripsi.skripsiservice.domain.SubmittedAnswerDomain;
import com.skripsi.skripsiservice.exception.RequestException;
import com.skripsi.skripsiservice.exception.UserNotFoundException;
import com.skripsi.skripsiservice.model.Course;
import com.skripsi.skripsiservice.model.Material;
import com.skripsi.skripsiservice.model.UserTable;
import com.skripsi.skripsiservice.repository.CourseRepository;
import com.skripsi.skripsiservice.repository.MaterialRepository;
import com.skripsi.skripsiservice.repository.PostRepository;
import com.skripsi.skripsiservice.repository.UserTableRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


@Service
public class UploadFileService {


    private static final String FTP_ADDRESS="23.101.27.104";
    private static final String LOGIN="skripsi";
    private static final String PSW="Test1234567890";
    private static final String DIR_PROFILE="/iati/photo_profile/";
    private static final String DIR_MATERIAL="/iati/material/";
    private static final String DIR_TEST="/iati/question/";
    private static final String DIR_TEST_ANSWER="/iati/answer/";
    private static final String DIR_ASSIGNMENT_ANSWER="/iati/assignment_answer/";
    private static final String DIR_POST="/iati/post/";
    private static final String DIR_COMMENT="/iati/comment/";


    public static final Logger LOGGER = LogManager.getLogger(UploadFileService.class.getName());

    @Autowired
    UserService userService;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserTableRepository userTableRepository;

    public void uploadImageProfileFTP(MultipartFile file, String userId) throws IOException {

        if(userService.getUserById(userId) == null){
            throw new UserNotFoundException("user with id " + userId + " is not found");
        }
        FTPClient con = new FTPClient();

        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_PROFILE+userId;
            con.makeDirectory(dirToCreate);
            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }


            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }

    public String getImage(String userId) throws IOException {
        FTPClient con = new FTPClient();

        String dirToGet = DIR_PROFILE+userId;

        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    InputStream is= con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    byte[] bytes = IOUtils.toByteArray(is);
                    return Base64.encodeBase64String(bytes);

                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }


    public String getTestQuestion(String courseId) throws Exception {
        FTPClient con = new FTPClient();

        String dirToGet = DIR_TEST+courseId;

        LOGGER.info("getting question");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    byte[] bytes = IOUtils.toByteArray(is);
                    LOGGER.info("SUCCES GET QUESTION BASE64");
                    return Base64.encodeBase64String(bytes);

                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }
    public String getTestAnswer(String userId,String courseId) throws Exception {
        FTPClient con = new FTPClient();

        String dirToGet = DIR_TEST_ANSWER+courseId;

        LOGGER.info("getting ANSWER");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            con.makeDirectory(dirToGet);
            dirToGet = dirToGet+"/"+userId;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    byte[] bytes = IOUtils.toByteArray(is);
                    return Base64.encodeBase64String(bytes);
                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }
    public List<SubmittedAnswerDomain> getUserTestSubmitted() throws Exception {
        FTPClient con = new FTPClient();

        List<SubmittedAnswerDomain> submittedAnswerDomains = new ArrayList<>();

        LOGGER.info("getting question");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            String dirToGet = DIR_TEST_ANSWER;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    dirToGet = DIR_TEST_ANSWER;
                    con.makeDirectory(dirToGet);
                    con.changeWorkingDirectory(dirToGet); //course
                    String courseId=ftpFile.getName();
                    dirToGet=dirToGet+"/"+ftpFile.getName();//userId
                    con.changeWorkingDirectory(dirToGet);
                    if (con.listFiles().length>=1){
                        FTPFile[] ftpFiles1=con.listFiles();
                        if (ftpFiles==null){
                            throw new Exception("no file");
                        }
                        for (FTPFile ftpFile1 : ftpFiles1){
                            SubmittedAnswerDomain submittedAnswerDomain = new SubmittedAnswerDomain();
                            Course course = courseRepository.findCourseByIdAndIsDeleted(courseId,"N");
                            UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(ftpFile1.getName(),"N");
                            if (userTable==null)
                                throw new RequestException("user not available");
                            submittedAnswerDomain.setUserName(userTable.getUsername());
                            submittedAnswerDomain.setCourseId(courseId);
                            submittedAnswerDomain.setCourseName(course.getCourseName());
                            submittedAnswerDomain.setUserId(ftpFile1.getName());
                            submittedAnswerDomains.add(submittedAnswerDomain);
                        }
                    }

                }
                return submittedAnswerDomains;
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }

    public List<String> getTestCourse() throws Exception {
        FTPClient con = new FTPClient();

        String dirToGet = DIR_TEST;

        List<String> strings = new ArrayList<>();
        LOGGER.info("getting question");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    strings.add(ftpFile.getName());
                    return strings;
                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }


    public void uploadmaterialFTP(MultipartFile file,String materialId) throws IOException, RequestException {

        if(materialRepository.findMaterialByIdAndIsDeleted(materialId,"N") == null){
            throw new RequestException("material with id " + materialId + " is not found");
        }
        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_MATERIAL+materialId;
            con.makeDirectory(dirToCreate);
            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }


            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }
    public void uploadAnswer(MultipartFile file,String userId,String courseId) throws IOException, RequestException {

        if(userService.getUserById(userId) == null){
            throw new UserNotFoundException("user with id " + userId + " is not found");
        }
        if (courseRepository.findCourseByIdAndIsDeleted(courseId,"N")==null){
            throw new RequestException("course not found");
        }
        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_TEST_ANSWER+courseId;
            con.makeDirectory(dirToCreate);
            dirToCreate = dirToCreate+"/"+userId;
            con.makeDirectory(dirToCreate);

            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    LOGGER.info(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }
            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }
    public void uploadFilePost(MultipartFile file,String postId) throws IOException, RequestException {

        if(postRepository.findPostApplicationByPostApplicationIdAndIsDeleted(postId,"N")==null){
            throw new RequestException("post not found for post id ="+postId);
        }
        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_POST+postId;
            con.makeDirectory(dirToCreate);

            System.out.println(postId);
            System.out.println(con.changeWorkingDirectory(dirToCreate));
            System.out.println("masok paeko");
            System.out.println(con.listFiles().length);
            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }
            System.out.println(con.storeFile(file.getOriginalFilename(), file.getInputStream()));
            con.logout();
            con.disconnect();
        }
    }

    public String getPostAttachment(String postId) throws Exception {
        FTPClient con = new FTPClient();

        LOGGER.info("getting post attachment");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);

            String dirToGet = DIR_POST+postId;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);

            if (con.listFiles().length>=1){
                LOGGER.info("masuk post");
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());

                    byte[] bytes = IOUtils.toByteArray(is);
                    LOGGER.info("converting...");
//                    return bytes;
                    String base64= Base64.encodeBase64String(bytes);
                    return base64;
                }
            }
            con.logout();
            con.disconnect();
        }
        return null;
    }

    public void uploadFileComment(MultipartFile file,String commentId) throws IOException, RequestException {

        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_COMMENT+commentId;
            con.makeDirectory(dirToCreate);

            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }
            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }

    public String getCommentAttachment(String commentId) throws Exception {
        FTPClient con = new FTPClient();


        LOGGER.info("getting post attachment");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);

            String dirToGet = DIR_COMMENT+commentId;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    byte[] bytes = IOUtils.toByteArray(is);
                    return Base64.encodeBase64String(bytes);

                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }
    public void uploadQuizAnswer(MultipartFile file,String userId,String materialId) throws IOException, RequestException {

        if(userService.getUserById(userId) == null){
            throw new UserNotFoundException("user with id " + userId + " is not found");
        }
        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_ASSIGNMENT_ANSWER;
            System.out.println(con.makeDirectory(dirToCreate));
            dirToCreate= dirToCreate+"/"+materialId;
            System.out.println(con.makeDirectory(dirToCreate));
            dirToCreate=dirToCreate+"/"+userId;
            System.out.println(con.makeDirectory(dirToCreate));
            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }
            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }

    public String getAssignmentAnswer(String userId,String materialId) throws Exception {
        FTPClient con = new FTPClient();


        LOGGER.info("getting assignment ANSWER");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);

            String dirToGet = DIR_ASSIGNMENT_ANSWER;
            con.makeDirectory(dirToGet);
            dirToGet= dirToGet+"/"+materialId;
            con.makeDirectory(dirToGet);
            dirToGet=dirToGet+"/"+userId;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println("yes");
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    byte[] bytes = IOUtils.toByteArray(is);
                    return Base64.encodeBase64String(bytes);

                }
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }


    public void uploadQuestion(MultipartFile file,String courseId) throws IOException, RequestException {


        if (courseRepository.findCourseByIdAndIsDeleted(courseId,"N")==null){
            throw new RequestException("course not found");
        }
        FTPClient con = new FTPClient();
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);


            String dirToCreate = DIR_TEST+courseId;
            con.makeDirectory(dirToCreate);
            con.changeWorkingDirectory(dirToCreate);

            if (con.listFiles().length>=1){
                FTPFile[] ftpFiles=con.listFiles();
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(con.retrieveFileStream(dirToCreate+"/"+ftpFile.getName()));
                    con.deleteFile(dirToCreate+"/"+ftpFile.getName());
                }
            }
            con.storeFile(file.getOriginalFilename(), file.getInputStream());
            con.logout();
            con.disconnect();
        }
    }
    public InputStream getMaterial(String postId, HttpServletResponse response) throws Exception {
        FTPClient con = new FTPClient();
        MaterialTypeCheck materialTypeCheck = new MaterialTypeCheck();

        LOGGER.info("getting post attachment");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);

            String dirToGet = DIR_MATERIAL+postId;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);

            if (con.listFiles().length>=1){
                LOGGER.info("masuk material");
                FTPFile[] ftpFiles=con.listFiles();
                if (ftpFiles==null){
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile: ftpFiles){
                    System.out.println(ftpFile.getName());
                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
                    if (ftpFile.getName().endsWith("pptx")){

                        response.setContentType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
                        response.setHeader("Content-Disposition", "attachment; filename="+ftpFile.getName());
                    }
                    else if (ftpFile.getName().endsWith("ppt")){
                        response.setContentType("application/vnd.ms-powerpoint");
                        response.setHeader("Content-Disposition", "attachment; filename="+ftpFile.getName());
                    }
                    return is;

                }
            }
            con.logout();
            con.disconnect();
        }
        return null;
    }

    public List<SubmittedAnswerDomain> getUserAssignmentAnswer() throws Exception {

        FTPClient con = new FTPClient();

        List<SubmittedAnswerDomain> submittedAnswerDomains = new ArrayList<>();

        LOGGER.info("getting question");
        con.connect(FTP_ADDRESS);

        if (con.login(LOGIN, PSW)) {

            con.enterLocalPassiveMode(); // important!
            con.setFileType(FTP.BINARY_FILE_TYPE);
            String dirToGet = DIR_ASSIGNMENT_ANSWER;
            con.makeDirectory(dirToGet);
            con.changeWorkingDirectory(dirToGet);
            if (con.listFiles().length >= 1) {
                FTPFile[] ftpFiles = con.listFiles();
                if (ftpFiles == null) {
                    throw new Exception("no file");
                }
                for (FTPFile ftpFile : ftpFiles) {
                    dirToGet = DIR_ASSIGNMENT_ANSWER;
                    con.makeDirectory(dirToGet);
                    con.changeWorkingDirectory(dirToGet); //course
                    String materialId = ftpFile.getName();
                    dirToGet = dirToGet + "/" + ftpFile.getName();//userId
                    con.changeWorkingDirectory(dirToGet);
                    LOGGER.info("test in");
                    if (con.listFiles().length >= 1) {
                        FTPFile[] ftpFiles1 = con.listFiles();
                        if (ftpFiles == null) {
                            throw new Exception("no file");
                        }
                        for (FTPFile ftpFile1 : ftpFiles1) {
                            SubmittedAnswerDomain submittedAnswerDomain = new SubmittedAnswerDomain();
                            UserTable userTable = userTableRepository.findUserTableByUserIdAndIsDeleted(ftpFile1.getName(), "N");
                            Material material = materialRepository.findMaterialByIdAndIsDeleted(materialId,"N");
                            if (userTable == null)
                                throw new RequestException("user not available");
                            submittedAnswerDomain.setUserName(userTable.getUsername());
                            submittedAnswerDomain.setCourseId(material.getCourse().getId());
                            submittedAnswerDomain.setMaterialName(material.getMaterialName());
                            submittedAnswerDomain.setMaterialId(material.getId());
                            submittedAnswerDomain.setCourseName(material.getCourse().getCourseName());
                            submittedAnswerDomain.setUserId(ftpFile1.getName());
                            submittedAnswerDomains.add(submittedAnswerDomain);
                        }
                    }

                }
                return submittedAnswerDomains;
            }
            con.logout();
            con.disconnect();

        }
        return null;
    }
//reserve jaga jaga
//    public String getMaterialPdf(String materialId)throws IOException{
//        FTPClient con = new FTPClient();
//
//        String dirToGet = DIR_MATERIAL+materialId;
//
//        con.connect(FTP_ADDRESS);
//
//        if (con.login(LOGIN, PSW)) {
//
//            con.enterLocalPassiveMode(); // important!
//            con.setFileType(FTP.BINARY_FILE_TYPE);
//            con.makeDirectory(dirToGet);
//            con.changeWorkingDirectory(dirToGet);
//            if (con.listFiles().length>=1){
//                FTPFile[] ftpFiles=con.listFiles();
//                for (FTPFile ftpFile: ftpFiles){
//                    System.out.println("yes");
//                    InputStream is =con.retrieveFileStream(dirToGet+"/"+ftpFile.getName());
//                    byte[] bytes = IOUtils.toByteArray(is);
//                    return Base64.encodeBase64String(bytes);
//
//                }
//            }
//            con.logout();
//            con.disconnect();
//
//        }
//        return null;
//    }

}
