package com.kpatil.jwdnd.cloudstorage.mapper;

import com.kpatil.jwdnd.cloudstorage.model.FileDAO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO files (filename, contenttype, filesize, userid, filedata) VALUES (#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer save(FileDAO fileDAO);

    @Select("SELECT * FROM files WHERE userid = #{userId}")
    List<FileDAO> findFilesByUserId(Integer userId);

    @Select("SELECT * FROM files WHERE fileId = #{fileId}")
    FileDAO findByFileId(Integer fileId);

    @Delete("DELETE FROM files WHERE fileId = #{fileId}")
    void deleteByFileId(Integer fileId);
}
