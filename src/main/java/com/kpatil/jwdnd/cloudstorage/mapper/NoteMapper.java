package com.kpatil.jwdnd.cloudstorage.mapper;

import com.kpatil.jwdnd.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("INSERT INTO notes (notetitle, notedescription, userid) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer save(Note note);

    @Select("SELECT * FROM notes WHERE userid = #{userId}")
    List<Note> findAllByUserId(Integer userId);

    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    List<Note> findByNoteId(Integer noteId);

    @Delete("DELETE FROM notes WHERE noteid = ${noteId} AND userId = #{userId}")
    Integer deleteByNoteId(int noteId, Integer userId);

    @Update("UPDATE notes SET notetitle = #{note.noteTitle}, notedescription = #{note.noteDescription} WHERE noteid = #{note.noteId} AND userId = #{userId}")
    Integer updateNote(Note note, Integer userId);
}
