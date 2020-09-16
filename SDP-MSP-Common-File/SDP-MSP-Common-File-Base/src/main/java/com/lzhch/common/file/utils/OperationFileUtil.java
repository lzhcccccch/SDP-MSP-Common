package com.lzhch.common.file.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @packageName： com.lzhch.common.file.utils
 * @className: OperationFileUtil
 * @description:  操作文件工具类
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 22:02
 */
public class OperationFileUtil {

    private static final String ENCODING = "utf-8";

    /**
     * 文件下载
     * @param filePath 文件路径
     * @return
     */
    public static ResponseEntity<byte[]> download(String filePath) {
        String fileName = FilenameUtils.getName(filePath);
        return downloadAssist(filePath, fileName);
    }

    /**
     * 文件下载
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    public static ResponseEntity<byte[]> download(String filePath, String fileName) {
        return downloadAssist(filePath, fileName);
    }

    /**
     * 文件下载辅助
     * @param filePath 文件路径
     * @param fileName 文件名
     * @return
     */
    private static ResponseEntity<byte[]> downloadAssist(String filePath, String fileName) {
        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IllegalArgumentException("filePath 参数必须是真实存在的文件路径:" + filePath);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, ENCODING));
            return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载辅助
     * @param b 文件路径
     * @return
     * @throws IOException
     */
    public static ResponseEntity<byte[]> downloadAssist(byte[] b, String filename) throws IOException {
        String fileName = UUIDUtil.getUUID() + filename;
        File file = FileUtil.byte2File(b,fileName);
        if (!file.isFile() || !file.exists()) {
            throw new IllegalArgumentException("filePath 参数必须是真实存在的文件路径:" + fileName);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileName, ENCODING));
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }

    /**
     * 多文件上传
     * @param request 当前上传的请求
     * @param basePath 保存文件的路径
     * @throws IOException
     * @throws IllegalStateException
     * @return Map<String, String> 返回上传文件的保存路径 以文件名做map的key;文件保存路径作为map的value
     */
    public static Map<String, String> multiFileUpload(HttpServletRequest request, String basePath) throws IllegalStateException, IOException {
        if (!(new File(basePath).isDirectory())) {
            throw new IllegalArgumentException("basePath 参数必须是文件夹路径");
        }
        return multifileUploadAssist(request, basePath, null);
    }

    /**
     * 多文件上传
     * @param request 当前上传的请求
     * @param basePath 保存文件的路径
     * @param exclude 排除文件名字符串,以逗号分隔的,默认无可传null
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    public static Map<String, String> multiFileUpload(HttpServletRequest request, String basePath, String exclude) throws IllegalStateException, IOException {
        if (!(new File(basePath).isDirectory())) {
            throw new IllegalArgumentException("basePath 参数必须是文件夹路径");
        }
        return multifileUploadAssist(request, basePath, exclude);
    }

    /**
     * 多文件上传辅助
     * @param request 当前上传的请求
     * @param basePath 保存文件的路径
     * @param exclude 排除文件名字符串,以逗号分隔的,默认无可传null
     * @return
     * @throws IOException
     */
    private static Map<String, String> multifileUploadAssist(HttpServletRequest request, String basePath, String exclude) throws IOException {
        exclude = exclude == null ? "" : exclude;

        Map<String, String> filePaths = new HashMap<String, String>();
        File file = null;
        // 创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        // 判断 request 是否有文件上传,即多部分请求
        if (multipartResolver.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
            // get the parameter names of the MULTIPART files contained in this request
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                // 取得上传文件
                List<MultipartFile> multipartFiles = multiRequest.getFiles(iter.next());
                for (MultipartFile multipartFile : multipartFiles) {
                    String fileName = multipartFile.getOriginalFilename();
                    if (!StringUtils.isEmpty(fileName) && (!exclude.contains(fileName))) {
                        String newName = changeFilename2UUID(fileName);
                        file = new File(basePath + newName);
                        filePaths.put(fileName, newName);
//                        filePaths.put(fileName, file.getPath());
                        multipartFile.transferTo(file);
                    }
                }
            }
        }
        return filePaths;
    }

    /**
     * 将文件名转变为UUID命名的, 保留文件后缀
     * @param filename
     * @return
     */
    public static String changeFilename2UUID(String filename) {
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + FilenameUtils.getExtension(filename);
    }

    /**
     * 删除文件
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
