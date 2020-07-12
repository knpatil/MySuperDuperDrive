package com.kpatil.jwdnd.cloudstorage.controller;

import com.kpatil.jwdnd.cloudstorage.model.Note;
import com.kpatil.jwdnd.cloudstorage.model.User;
import com.kpatil.jwdnd.cloudstorage.services.NoteService;
import com.kpatil.jwdnd.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/note")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
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
        return "/home";
    }

}
