package com.project.team;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String filePath = uploadPath + File.separator + fileName;
        file.transferTo(new File(filePath));
        return filePath;
    }
}
