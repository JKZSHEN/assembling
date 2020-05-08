package com.jk.assembling.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jk.sftp")
public class SftpProperties {
    /**
     * sftp
     */
    private String host = "127.0.0.1";
    /**
     * sftp username
     */
    private String username = "root";
    /**
     * sftp password
     */
    private String password = "root";
    /**
     * sftp port
     */
    private int port = 22;
    /**
     * sftp privateKey
     */
    private String privateKey = "";
    /**
     * sftp directory
     */
    private String directory = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
