package com.skripsi.skripsiservice.controller;

import com.skripsi.skripsiservice.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@RestController
@CrossOrigin
@RequestMapping("/upload-file")
public class DownloadController {

    @Autowired
    UploadFileService uploadFileService;

    @RequestMapping(value = "/getMaterial/{materialId}", method = RequestMethod.GET
    )
    public void getMaterial(@PathVariable String materialId, HttpServletResponse response) throws Exception {

        InputStream inputStream = uploadFileService.getMaterial(materialId,response);
        int nRead;
        while ((nRead = inputStream.read()) != -1) {
            response.getWriter().write(nRead);
        }
    }
}
