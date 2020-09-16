package com.lzhch.file.fastdfs.wrapper;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @packageName： com.lzhch.file.fastdfs.wrapper
 * @className: FastDFSClientWrapper
 * @description:  FastDFS 文件上传下载包装类
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 22:25
 */
@Component
public class FastDFSClientWrapper {

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * @description: 文件上传
     * @param: [file 文件信息, fileType 文件类型]
     * @return: java.lang.String
     * @author: liuzhichao 2020-07-29
     * 这里抛出异常, 在 service 中才可以进行捕捉从而对上传结果进行判断
     */
    public String uploadFile(MultipartFile file, String fileType) throws IOException {
        StorePath storePath = storageClient.uploadFile(file.getInputStream(),file.getSize(), fileType,null);
        return getResAccessUrl(storePath);
    }

    /**
     * @description:  文件上传 自动切割生成文件类型
     * @param: [file 文件信息]
     * @return: java.lang.String
     * @author: liuzhichao 2020-08-31 10:50
     */
    public String uploadFile(MultipartFile file){
        StorePath storePath = null;
        try {
            String fileType = FilenameUtils.getExtension(file.getOriginalFilename());
            storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), fileType, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResAccessUrl(storePath);
    }

    /**
     * @description:  文件上传
     * @param: [file]
     * @return: java.lang.String
     * @author: liuzhichao 2020-08-31 11:03
     */
    public String uploadFile(File file) {
        try {
            FileInputStream fs = new FileInputStream(file);
            String fileType = FilenameUtils.getExtension(file.getName());
            StorePath storePath = storageClient.uploadFile(fs, fs.available(), fileType, null);
            return getResAccessUrl(storePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description:  文件上传, 将一个字符串生成一个文件
     * @param: [content 文件内容, fileExtension 文件类型]
     * @return: java.lang.String
     * @author: liuzhichao 2020-08-31 15:00
     */
    public String uploadFile(String content, String fileExtension) {
        byte[] buff = content.getBytes(Charset.defaultCharset());
        ByteArrayInputStream stream = new ByteArrayInputStream(buff);
        StorePath storePath = storageClient.uploadFile(stream, buff.length, fileExtension, null);
        return getResAccessUrl(storePath);
    }

    /**
     * @description:  封装图片完整URL地址
     * @param: [storePath]
     * @return: java.lang.String
     * @author: liuzhichao 2020-08-31 15:01
     */
    private String getResAccessUrl(StorePath storePath) {
        // fdfsWebServer.getWebServerUrl() 需要在配置文件中配置 fdfs.web-server-url (配置为 nginx 地址)
        // String fileUrl = fdfsWebServer.getWebServerUrl() + storePath.getFullPath();
        String fileUrl = storePath.getFullPath();
        // 返回的路径可以在浏览器中直接进行访问
        return fileUrl;
    }

    /**
     * @description:  文件下载
     * @param: [fileUrl] 文件在 fastdfs 上存储的路径 group0/xx/xx/
     * @return: byte[]
     * @author: liuzhichao 2020-08-31 15:01
     */
    public byte[] downloadFile(String fileUrl) {
        String group = fileUrl.substring(0, fileUrl.indexOf("/"));
        String path = fileUrl.substring(fileUrl.indexOf("/") + 1);
        byte[] bytes = storageClient.downloadFile(group, path, new DownloadByteArray());
        return bytes;
    }

    /**
     * @description:  删除文件
     * @param: [filename] 文件在 fastdfs 上存储的路径 group0/xx/xx
     * @return: void
     * @author: liuzhichao 2020-08-31 15:03
     */
    public void delete(String filename){
        if (StringUtils.isEmpty(filename)) {
            return ;
        }
        storageClient.deleteFile(filename);
    }

}
