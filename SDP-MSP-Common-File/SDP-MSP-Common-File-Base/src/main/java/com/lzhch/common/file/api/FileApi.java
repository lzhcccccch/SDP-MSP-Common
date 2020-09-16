package com.lzhch.common.file.api;

import com.lzhch.common.file.dto.FileRes;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @packageName： com.lzhch.common.file.api
 * @className: FileApi
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 14:50
 */
public interface FileApi {

    /**
     * @description:  大文件分片上传(单个文件)
     * @param: [request]
     * @return: java.util.Map
     * @author: liuzhichao 2020-08-25 16:26
     */             
    public Map SlicesUpload (HttpServletRequest request);

    /**
     * @description:  文件上传(支持多文件上传)
     * @param: [request]
     * @return: com.lzhch.common.file.dto.FileRes
     * @author: liuzhichao 2020-08-25 16:27
     */       
    public FileRes fileUpload(HttpServletRequest request);

    /**
     * @description:  文件上传(Base64)
     * @param: [request]
     * @return: com.lzhch.common.file.dto.FileRes
     * @author: liuzhichao 2020-08-25 16:28
     */       
    public FileRes fileUpload(String request);

    /**
     * @description:  文件上传, 将一个字符串生成一个文件
     * @param: [content 文件内容, fileExtension 文件类型]
     * @return: com.lzhch.common.file.dto.FileRes
     * @author: liuzhichao 2020-08-31 15:33
     */
    public FileRes fileUpload(String content, String fileType);

    /**
     * @description:  大文件分片下载
     * @param path 所有分片文件的下载路径和大小, 格式为 "url,size;url,size;url,size", url 要有序
     * @param fileOriginalName 原始文件名字 为了下载到本地可以是原始名字
     * @return: org.springframework.http.ResponseEntity<byte[]>
     * @author: liuzhichao 2020-08-25 16:38
     */
    public ResponseEntity<byte[]> download(String path, String fileOriginalName);

    /**
     * @description:  单个文件下载
     * @param: [filePath] 文件在 fastdfs 中存储路径
     * @return: org.springframework.http.ResponseEntity<byte[]>
     * @author: liuzhichao 2020-08-25 16:41
     */
    public ResponseEntity<byte[]> download(String filePath);

    /**
     * @description:  删除文件
     * @param: [filePath]
     * @return: void
     * @author: liuzhichao 2020-08-25 16:47
     */
    public void delete(String filePath);

}
