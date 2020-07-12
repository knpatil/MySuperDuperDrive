package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.FileDAO;
import com.kpatil.jwdnd.cloudstorage.model.User;
import com.kpatil.jwdnd.cloudstorage.services.FileService;
import com.kpatil.jwdnd.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication auth, Model model) throws IOException {
        logger.info("Received request to upload a file " + file.getOriginalFilename());
        logger.info("Checking user ");
        User user = this.userService.getUser(auth.getName());
        logger.info("This user is " + user.getUsername());
        if (file.getOriginalFilename() != null && !Objects.equals(file.getOriginalFilename(), "")) {
            FileDAO fileDAO = this.fileService.uploadFile(file, user.getUserId());
            logger.info("File uploaded successfully with ID = " + fileDAO.getFileId());
        } else {
            model.addAttribute("message", "No file to upload!");
        }
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        return "home";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Integer fileId) {
        logger.info("Received request to download file with id = " + fileId);
        FileDAO fileDAO = this.fileService.getFileById(fileId);
        String contentType = fileDAO.getContentType();
        String fileName = fileDAO.getFilename();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + fileName + "\"")
                .body(fileDAO.getFileData());
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        logger.info("Received request to delete file with id = " + fileId);
        try {
            this.fileService.deleteFile(fileId);
        } catch (Exception e) {
            logger.warn("Error occurred while deleting file: " + Arrays.toString(e.getStackTrace()));
        }
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        return "home";
    }

}
