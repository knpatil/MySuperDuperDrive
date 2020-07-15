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

    public void updateCredential(Credential credential) {
        logger.info("Updating credential for id " + credential.getCredentialId());
        encryptPassword(credential);
        int credentialId = this.credentialMapper.updateCredential(credential);
        logger.info("Credential updated for id " + credentialId);
    }

    public Credential addCredential(Credential credential, Integer userId) {
        logger.info("Adding new credential ...");
        credential.setUserId(userId);
        encryptPassword(credential);
        this.credentialMapper.save(credential);
        logger.info("credential added.");
        return credential;
    }

    public List<Credential> getAllCredentials(Integer userId) {
        logger.info("Fetching all credentials for user " + userId);
        List<Credential> credentials = this.credentialMapper.findAllByUserId(userId);
        // decrypt password for display
        return credentials.stream().map(this::decryptPassword).collect(Collectors.toList());
    }

    public void deleteCredential(Integer credentialId) {
        logger.info("Deleting credential " + credentialId);
        this.credentialMapper.deleteByCredentialId(credentialId);
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
        credential.setPassword(encryptionService.decryptValue(credential.getPassword(), credential.getKey()));
        return credential;
    }

    public Credential getCredentialById(int credentialId) {
        return credentialMapper.findByCredentialId(credentialId);
    }
}
