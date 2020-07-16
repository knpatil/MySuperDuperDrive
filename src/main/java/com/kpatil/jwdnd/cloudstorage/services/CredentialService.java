package com.kpatil.jwdnd.cloudstorage.services;

import com.kpatil.jwdnd.cloudstorage.mapper.CredentialMapper;
import com.kpatil.jwdnd.cloudstorage.model.Credential;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private static final Logger logger = LoggerFactory.getLogger(CredentialService.class);
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public Integer updateCredential(Credential credential, Integer userId) {
        logger.info("Updating credential for id " + credential.getCredentialId());
        encryptPassword(credential);
        Integer returnCode = this.credentialMapper.updateCredential(credential, userId);
        logger.info("Credential updated with return code : " + returnCode);
        return returnCode;
    }

    public int addCredential(Credential credential, Integer userId) {
        logger.info("Adding new credential ...");
        credential.setUserId(userId);
        encryptPassword(credential);
        try {
            int returnCode = this.credentialMapper.save(credential);
            logger.info("credential added with return code : " + returnCode);
            return returnCode; // success
        } catch (Exception e) {
            logger.warn("Failed to save credential : " + e.getMessage());
            return 0;
        }
    }

    public List<Credential> getAllCredentials(Integer userId) {
        logger.info("Fetching all credentials for user " + userId);
        List<Credential> credentials = this.credentialMapper.findAllByUserId(userId);
        // decrypt password for display
        return credentials.stream().map(this::decryptPassword).collect(Collectors.toList());
    }

    public Integer deleteCredential(Integer credentialId, Integer userId) {
        logger.info("Deleting credential " + credentialId);
        return this.credentialMapper.deleteByCredentialId(credentialId, userId);
    }

    private void encryptPassword(Credential credential) {
        // set random key
        String key = RandomStringUtils.random(16, true, true);
        logger.info("using key " + key);
        credential.setKey(key);
        // store encrypted password in db
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), key));
    }

    private Credential decryptPassword(Credential credential) {
        credential.setTextPwd(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    public Credential getCredentialById(int credentialId) {
        return credentialMapper.findByCredentialId(credentialId);
    }
}
