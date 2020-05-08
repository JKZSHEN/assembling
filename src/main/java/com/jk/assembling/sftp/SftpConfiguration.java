package com.jk.assembling.sftp;

import com.jk.assembling.properties.SftpProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(TemplateSftp.class)
@EnableConfigurationProperties(SftpProperties.class)
public class SftpConfiguration {
        @Autowired
        private SftpProperties sftpProperties;

    @Bean
    public TemplateSftp sftp(){
        TemplateSftp sftp = new TemplateSftp();
        sftp.setSftpHost(sftpProperties.getHost());
        sftp.setSftpUsername(sftpProperties.getUsername());
        sftp.setSftpPassword(sftpProperties.getPassword());
        sftp.setSftpPort(sftpProperties.getPort());
        sftp.setSftpDirectory(sftpProperties.getDirectory());
        return sftp;
    }

}
