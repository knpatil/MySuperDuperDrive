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

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int uploadFile(MultipartFile file, Integer userId) throws IOException {
        logger.info("Uploading file " + file.getOriginalFilename());
        FileDAO newFileDAO = new FileDAO(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getSize(),
                userId,
                file.getBytes()
        );
        try {
            this.fileMapper.save(newFileDAO);
            logger.info("Uploaded");
            return 1;
        } catch (Exception e) {
            logger.info("File save failed: " + e.getMessage());
            return 0;
        }
    }

    public List<FileDAO> getAllFiles(Integer userId) {
        logger.info("Fetching all files for user " + userId);
        return this.fileMapper.findFilesByUserId(userId);
    }

    public FileDAO getFileById(Integer fileId, Integer userId) {
        logger.info("Fetching file with id " + fileId + " for user " + userId);
        return this.fileMapper.findByFileId(fileId, userId);
    }

    public Integer deleteFile(Integer fileId, Integer userId) {
        logger.info("Deleting file with id " + fileId + " for userId " + userId);
        return this.fileMapper.deleteByFileId(fileId, userId);
    }
}
