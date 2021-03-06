package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.FileDAO;
import com.kpatil.jwdnd.cloudstorage.model.User;
import com.kpatil.jwdnd.cloudstorage.services.CredentialService;
import com.kpatil.jwdnd.cloudstorage.services.FileService;
import com.kpatil.jwdnd.cloudstorage.services.NoteService;
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
import java.util.Objects;

@Controller
@RequestMapping("/file")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public FileController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, Authentication auth, Model model) throws IOException {
        logger.info("Received request to upload a file " + file.getOriginalFilename());
        logger.info("Checking user ");
        User user = this.userService.getUser(auth.getName());
        logger.info("This user is " + user.getUsername());
        if (file.getOriginalFilename() != null && !Objects.equals(file.getOriginalFilename(), "")) {
            int returnCode = this.fileService.uploadFile(file, user.getUserId());
            if (returnCode == 0) {
                logger.info("File upload failed.");
                model.addAttribute("message", "Duplicate file!");
            } else {
                logger.info("File uploaded successfully.");
                model.addAttribute("successMessage", "File uploaded successfully.");
            }
        } else {
            model.addAttribute("message", "No file selected to upload!");
        }
        addModelAttributes(model, user);
        return "home";
    }

    @GetMapping("/view/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileId") Integer fileId, Authentication auth) {
        logger.info("Received request to download file with id = " + fileId);
        User user = userService.getUser(auth.getName());
        logger.info("User is " + user.getUsername());
        FileDAO fileDAO = this.fileService.getFileById(fileId, user.getUserId());
        if (null != fileDAO) {
            String contentType = fileDAO.getContentType();
            String fileName = fileDAO.getFilename();
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + fileName + "\"")
                    .body(fileDAO.getFileData());
        } else {
            return ResponseEntity.badRequest().body("Unauthorized access!");
        }
    }

    @GetMapping("/delete/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Authentication auth, Model model) {
        User user = userService.getUser(auth.getName());
        logger.info("Received request to delete file with id = " + fileId);
        Integer returnCode = this.fileService.deleteFile(fileId, user.getUserId());
        if (returnCode == 0) {
            model.addAttribute("message", "Unauthorized access or file not found!");
        } else {
            model.addAttribute("successMessage", "File deleted successfully.");
        }
        addModelAttributes(model, user);
        return "home";
    }

    private void addModelAttributes(Model model, User user) {
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("activeTab", "#nav-files");
    }

}
