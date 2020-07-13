package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.User;
import com.kpatil.jwdnd.cloudstorage.services.CredentialService;
import com.kpatil.jwdnd.cloudstorage.services.FileService;
import com.kpatil.jwdnd.cloudstorage.services.NoteService;
import com.kpatil.jwdnd.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/home", "/"})
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final FileService fileService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final UserService userService;

    public HomeController(FileService fileService, NoteService noteService, CredentialService credentialService, UserService userService) {
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @GetMapping
    public String homePageView(Authentication auth, Model model) {
        logger.info("Received request for home page ..");
        String userName = auth.getName();
        logger.info("Logged in user -> " + userName);
        try {
            User user = userService.getUser(userName);
            model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
            model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
            model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
            model.addAttribute("credentials", this.credentialService.getAllCredentials(user.getUserId()));
            model.addAttribute("activeTab", "#nav-files");
        } catch (Exception e) {
            logger.info("User session error: " + e.getMessage());
            return "redirect:/login";
        }
        return "home";
    }
}
