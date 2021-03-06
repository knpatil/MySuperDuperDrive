package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.Credential;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credential")
public class CredentialController {

    private static final Logger logger = LoggerFactory.getLogger(CredentialController.class);

    private final CredentialService credentialService;
    private final NoteService noteService;
    private final FileService fileService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, NoteService noteService, FileService fileService, UserService userService) {
        this.credentialService = credentialService;
        this.noteService = noteService;
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateCredential(Credential credential, Authentication auth, Model model) {
        logger.info("Received request to create or update credential ...");
        User user = this.userService.getUser(auth.getName());
        if (credential.getCredentialId() > 0) {
            Integer rc = this.credentialService.updateCredential(credential, user.getUserId());
            logger.info("Return code for update => " + rc);
            if (rc == 0) {
                model.addAttribute("message", "Error in updating a credential!");
            } else {
                model.addAttribute("successMessage", "Credential updated successfully.");
            }
        } else {
            int returnCode = this.credentialService.addCredential(credential, user.getUserId());
            if (returnCode == 0) {
                model.addAttribute("message", "Duplicate URL or error in adding new credential!");
            } else {
                model.addAttribute("successMessage", "Credential added successfully.");
            }
        }
        addModelAttributes(model, user);
        return "/home";
    }

    @GetMapping("/delete/{credentialId}")
    public String deleteCredential(@PathVariable("credentialId") Integer credentialId, Authentication auth, Model model) {
        logger.info("Received request to delete credential with ID = " + credentialId);
        User user = this.userService.getUser(auth.getName());
        Integer returnCode = this.credentialService.deleteCredential(credentialId, user.getUserId());
        if (returnCode == 0) {
            logger.warn("Unauthorized user trying to delete this resource!");
            model.addAttribute("message", "Unauthorized operation!");
        } else {
            model.addAttribute("successMessage", "Credential deleted!");
        }
        addModelAttributes(model, user);
        return "home";
    }

    private void addModelAttributes(Model model, User user) {
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("credentials", this.credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("activeTab", "#nav-credentials");
    }

}
