package com.kpatil.jwdnd.cloudstorage.services;

import com.kpatil.jwdnd.cloudstorage.mapper.FileMapper;
import com.kpatil.jwdnd.cloudstorage.model.FileDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public FileDAO uploadFile(MultipartFile file, Integer userId) throws IOException {
        logger.info("Uploading file " + file.getOriginalFilename());
        FileDAO newFileDAO = new FileDAO(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                userId,
                file.getBytes()
        );
        fileMapper.save(newFileDAO);
        logger.info("Uploaded");
        return newFileDAO;
    }

    public List<FileDAO> getAllFiles(Integer userId) {
        return fileMapper.findFilesByUserId(userId);
    }
}
