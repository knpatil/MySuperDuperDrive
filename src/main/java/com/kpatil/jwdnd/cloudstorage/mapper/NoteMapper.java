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

    @Delete("DELETE FROM notes WHERE noteid = ${noteId}")
    void deleteByNoteId(int noteId);

    @Update("UPDATE notes SET notetitle = #{noteTitle}, notedescription = #{noteDescription} WHERE noteid = #{noteId}")
    int updateNote(Note note);
}
