package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.Note;
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
@RequestMapping("/note")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final FileService fileService;
    private final CredentialService credentialService;
    private final UserService userService;

    public NoteController(NoteService noteService, FileService fileService, CredentialService credentialService, UserService userService) {
        this.noteService = noteService;
        this.fileService = fileService;
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    public String createOrUpdateNote(Note note, Authentication auth, Model model) {
        logger.info("Received request to create or update note ...");
        User user = this.userService.getUser(auth.getName());
        if (note.getNoteId() > 0) {
            Integer rc = this.noteService.updateNote(note, user.getUserId());
            logger.info("Return code is " + rc);
            if (rc == 0) {
                model.addAttribute("message", "Error in updating a note!");
            } else {
                model.addAttribute("successMessage", "Note updated successfully.");
            }
        } else {
            int returnCode = this.noteService.addNote(note, user.getUserId());
            if (returnCode == 0) {
                model.addAttribute("message", "Error in adding new note!");
            } else {
                model.addAttribute("successMessage", "Note added successfully.");
            }
        }
        addModelAttributes(model, user);
        return "/home";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication auth, Model model) {
        logger.info("Received request to delete note with ID = " + noteId);
        User user = this.userService.getUser(auth.getName());
        Integer returnCode = this.noteService.deleteNote(noteId, user.getUserId());
        if (returnCode == 0) {
            logger.warn("Unauthorized user trying to delete this resource!");
            model.addAttribute("message", "Unauthorized operation!");
        } else {
            model.addAttribute("successMessage", "Note deleted!");
        }
        addModelAttributes(model, user);
        return "home";
    }

    private void addModelAttributes(Model model, User user) {
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("credentials", this.credentialService.getAllCredentials(user.getUserId()));
        model.addAttribute("activeTab", "#nav-notes");
    }

}
