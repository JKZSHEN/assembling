package com.jk.assembling.sftp;

import com.jcraft.jsch.*;
import com.jk.assembling.utli.StringUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

@Data
public class TemplateSftp {
    private Logger log = LoggerFactory.getLogger(TemplateSftp.class);
    /**
     * 服务器地址
     */
    private String sftpHost;
    /**
     * 服务器IP
     */
    private String sftpUsername;
    /**
     * 服务器密码
     */
    private String sftpPassword;
    /**
     * 私钥key
     */
    private String sftpPrivateKey;
    /**
     * 服务器端口号
     */
    private int sftpPort;

    /**
     * 服务器保存目录
     */
    private String sftpDirectory;

    private ChannelSftp sftp;

    private Session session;

    /**
     * 连接服务器
     */
    public void login(){
        JSch jsch = new JSch();
        try {
            if (StringUtils.isNotEmpty(sftpPrivateKey)) {
                    jsch.addIdentity(sftpPrivateKey);// 设置私钥
            }
            session = jsch.getSession(sftpUsername, sftpHost, sftpPort);
            if (StringUtils.isNotEmpty(sftpPassword)) {
                session.setPassword(sftpPassword);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            log.info("successFull");
        } catch (JSchException e) {
            log.error("sftp连接服务器异常",e);
        }
    }

    /**
     * 关闭连接
     */
    public void logout(){
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
            }
        }
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * 检查文件夹是否存在
     * @param path
     * @return
     */
    public boolean isExistDir(String path){
        boolean  isExist=false;
        try {
            SftpATTRS sftpATTRS = sftp.lstat(path);
            isExist = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isExist = false;
            }
        }
        return isExist;

    }

    /**
     * 检查目录
     * @param directory
     * @return
     */
    public String verify(String directory){
        if(StringUtils.isEmpty(directory)){
            return "保存文件的目录不能为空 {}";
        }
        String dir = directory.substring(0, directory.lastIndexOf("/data2") +6);
        if(!dir.equals("/data2")){
            return "{ 需要保存文件的目录必须包含/data2 /data2必须为根目录 }"+dir;
        }
        return null;
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     * @param sftpFileName
     *            sftp端文件名
     * @param input
     *            输入流
     * @throws SftpException
     * @throws Exception
     */
    public String upload(InputStream input, String sftpFileName) throws SftpException {
        String verify = verify(sftpDirectory);
        if(null != verify){
            return verify;
        }
        if(StringUtils.isEmpty(sftpFileName)){
            sftpFileName = StringUtils.getFileName(sftpFileName);
        }
        if(StringUtils.isSerifySuffix(sftpFileName)){
            return "没有后缀文件名";
        }
        this.login();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String date = format.format(new Date());
            sftpDirectory = sftpDirectory + "/" + date;
            sftp.cd(sftpDirectory);
        } catch (SftpException e) {
            log.warn("directory is not exist");
            sftp.mkdir(sftpDirectory);
            sftp.cd(sftpDirectory);
        }
        sftp.put(input, sftpFileName);
        log.info("file:{} is upload successful" , sftpFileName);
        return sftpDirectory.replace("/data2", "")+"/"+sftpFileName;
    }

    /**
     * 上传单个文件
     * @param uploadFile
     *            要上传的文件,包括路径
     * @throws FileNotFoundException
     * @throws SftpException
     * @throws Exception
     */
    public String upload( String uploadFile) throws FileNotFoundException, SftpException{
        File file = new File(uploadFile);
        return upload( new FileInputStream(file), file.getName());
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param byteArr
     *            要上传的字节数组
     * @throws SftpException
     * @throws Exception
     */
    public String upload( String sftpFileName, byte[] byteArr) throws SftpException{
        return upload(new ByteArrayInputStream(byteArr), sftpFileName);
    }
    /**
     * 将字符串按照指定的字符编码上传到sftp
     * @param sftpFileName
     *            文件在sftp端的命名
     * @param dataStr
     *            待上传的数据
     * @param charsetName
     *            sftp上的文件，按该字符编码保存
     * @throws UnsupportedEncodingException
     * @throws SftpException
     * @throws Exception
     */
    public String upload( String sftpFileName, String dataStr, String charsetName) throws UnsupportedEncodingException, SftpException{
        return upload( new ByteArrayInputStream(dataStr.getBytes(charsetName)), sftpFileName);
    }

    /**
     * 删除文件
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @throws SftpException
     * @throws Exception
     */
    public String delete(String directory, String deleteFile) throws SftpException{
        String verify = verify(directory);
        if(null != verify){
            return verify;
        }
        this.login();
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        }finally {
            this.logout();
        }
        return "success";
    }

    /**
     * 列出目录下的文件
     * @param directory
     *            要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector<?> listFiles(String directory) throws SftpException {
        this.login();
        Vector list = null;
        try {
            list = sftp.ls(directory);
        }finally {
            this.logout();
        }
        return list;
    }

}
