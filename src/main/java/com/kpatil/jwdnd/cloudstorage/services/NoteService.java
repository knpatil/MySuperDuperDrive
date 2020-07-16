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

    public Integer updateNote(Note note, Integer userId) {
        logger.info("Updating note with id " + note.getNoteId() + " for user " + userId);
        int returnCode = this.noteMapper.updateNote(note, userId);
        logger.info("Update return code: " + returnCode);
        return returnCode;
    }

    public int addNote(Note note, Integer userId) {
        logger.info("Adding new note ...");
        note.setUserId(userId);
        int returnCode = this.noteMapper.save(note);
        logger.info("note added with return code : " + returnCode);
        logger.info("note id is " + note.getNoteId());
        return returnCode;
    }

    public List<Note> getAllNotes(Integer userId) {
        logger.info("Fetching all notes for user " + userId);
        return this.noteMapper.findAllByUserId(userId);
    }

    public Integer deleteNote(Integer noteId, Integer userId) {
        logger.info("Deleting note with id " + noteId + " for user " + userId);
        return this.noteMapper.deleteByNoteId(noteId, userId);
    }
}
