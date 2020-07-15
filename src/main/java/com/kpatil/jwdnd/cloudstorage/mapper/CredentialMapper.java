package com.kpatil.jwdnd.cloudstorage.mapper;

import com.kpatil.jwdnd.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("INSERT INTO credentials (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    Integer save(Credential credential);

    @Select("SELECT * FROM credentials WHERE userid = #{userId}")
    List<Credential> findAllByUserId(Integer userId);

    @Select("SELECT * FROM credentials WHERE credentialid = #{credentialId}")
    Credential findByCredentialId(Integer credentialId);

    @Delete("DELETE FROM credentials WHERE credentialid = ${credentialId} AND userId = #{userId}")
    Integer deleteByCredentialId(int credentialId, Integer userId);

    @Update("UPDATE credentials SET url = #{credential.url}, username = #{credential.username}, key = #{credential.key}, password = #{credential.password} WHERE credentialid = #{credential.credentialId} AND userId = #{userId}")
    Integer updateCredential(Credential credential, Integer userId);

}
