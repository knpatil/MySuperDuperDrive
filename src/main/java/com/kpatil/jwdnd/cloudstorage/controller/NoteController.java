package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.Note;
import com.kpatil.jwdnd.cloudstorage.model.User;
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
    private final UserService userService;
    private final FileService fileService;

    public NoteController(NoteService noteService, UserService userService, FileService fileService) {
        this.noteService = noteService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping
    public String createOrUpdateNote(Note note, Authentication auth, Model model) {
        logger.info("Received request to create or update note ...");
        User user = this.userService.getUser(auth.getName());
        if (note.getNoteId() > 0) {
            this.noteService.updateNote(note);
        } else {
            Note newNote = this.noteService.addNote(note, user.getUserId());
            logger.info("Note created with id = " + newNote.getNoteId());
        }
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("activeTab", "#nav-notes");
        return "/home";
    }

    @GetMapping("/delete/{noteId}")
    public String deleteNote(@PathVariable("noteId") Integer noteId, Authentication auth, Model model) {
        logger.info("Received request to delete note with ID = " + noteId);
        User user = this.userService.getUser(auth.getName());
        try {
            this.noteService.deleteNote(noteId);
        } catch (Exception e) {
            logger.info("Exception occurred while deleting note " + e.getMessage());
            model.addAttribute("message", e.getMessage());
        }
        model.addAttribute("welcomeText", "Welcome " + user.getFirstName());
        model.addAttribute("notes", this.noteService.getAllNotes(user.getUserId()));
        model.addAttribute("files", this.fileService.getAllFiles(user.getUserId()));
        model.addAttribute("activeTab", "#nav-notes");
        return "home";
    }

}
