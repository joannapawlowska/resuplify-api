package com.io.resuplifyapi.config;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.hibernate5.encryptor.HibernatePBEEncryptorRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    public JasyptConfig(@Value("${jasypt.secret.key}") final String jasyptSecretKey) {

        PooledPBEStringEncryptor strongEncryptor = new PooledPBEStringEncryptor();
        strongEncryptor.setPoolSize(4);
        strongEncryptor.setPassword(jasyptSecretKey);
        strongEncryptor.setAlgorithm("PBEWithMD5AndDES");

        HibernatePBEEncryptorRegistry registry = HibernatePBEEncryptorRegistry.getInstance();
        registry.registerPBEStringEncryptor("stringEncryptor", strongEncryptor);
    }
}