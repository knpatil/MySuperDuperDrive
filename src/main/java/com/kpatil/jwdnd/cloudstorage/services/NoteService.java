package com.kpatil.jwdnd.cloudstorage.services;

import com.kpatil.jwdnd.cloudstorage.mapper.NoteMapper;
import com.kpatil.jwdnd.cloudstorage.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private static final Logger logger = LoggerFactory.getLogger(NoteService.class);
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void updateNote(Note note) {
        logger.info("Updating note for id " + note.getNoteId());
        int noteId = this.noteMapper.updateNote(note);
        logger.info("Note updated for id " + noteId);
    }

    public Note addNote(Note note, Integer userId) {
        logger.info("Adding new note ...");
        note.setUserId(userId);
        this.noteMapper.save(note);
        logger.info("note added.");
        return note;
    }

    public List<Note> getAllNotes(Integer userId) {
        logger.info("Fetching all notes for user " + userId);
        return this.noteMapper.findAllByUserId(userId);
    }

    public void deleteNote(Integer noteId) {
        logger.info("Deleting note " + noteId);
        this.noteMapper.deleteByNoteId(noteId);
    }
}
