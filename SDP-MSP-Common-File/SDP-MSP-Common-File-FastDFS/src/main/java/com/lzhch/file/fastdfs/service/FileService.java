package com.lzhch.file.fastdfs.service;

import com.lzhch.common.file.api.FileApi;
import com.lzhch.common.file.config.FileConfig;
import com.lzhch.common.file.dto.FileRes;
import com.lzhch.common.file.dto.FileSlicesFdfsRes;
import com.lzhch.common.file.utils.Base64Utils;
import com.lzhch.file.fastdfs.wrapper.FastDFSClientWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @packageName： com.lzhch.file.fastdfs.service
 * @className: FileService
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 22:16
 */
@Service
public class FileService implements FileApi {

    private static final String ENCODING = "utf-8";

    @Autowired
    FastDFSClientWrapper fastDFSClientWrapper;

    public Map SlicesUpload(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        // 获得文件分片数据
        MultipartFile file = multipartRequest.getFile("data");
        // 分片第几片
        int index = Integer.parseInt(multipartRequest.getParameter("index"));
        // 总片数
        int total = Integer.parseInt(multipartRequest.getParameter("total"));
        // 获取文件名
        String fileFullName = multipartRequest.getParameter("name");
        int lastIndexOf = fileFullName.lastIndexOf(".");
        // 文件名字, 不带类型
        String fileName = fileFullName.substring(0, lastIndexOf);
        String fileType = fileFullName.substring(lastIndexOf + 1);
        // fastdfs 存储路径
        String url = "";
        if (index < total) {
            try {
                url = fastDFSClientWrapper.uploadFile(file, fileType);
                System.out.println("----slicesUrl[" + index + "]:" + url);
                map.put("status", 201);
                map.put("fileUrl", url);
            } catch (IOException e) {
                e.printStackTrace();
                map.put("status", 502);
            }
        } else {
            try {
                url = fastDFSClientWrapper.uploadFile(file, fileType);
                System.out.println("----slicesUrl[" + index + "]:" + url);
                map.put("status", 200);
                map.put("fileUrl", url);
            } catch (IOException e) {
                e.printStackTrace();
                map.put("status", 502);
            }
        }
        return map;
    }

    public FileRes fileUpload(HttpServletRequest request) {
        Map<String, String> filePaths = new HashMap<String, String>();
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
                    if (StringUtils.isNotEmpty(fileName)) {
                        String newName = fastDFSClientWrapper.uploadFile(multipartFile);
                        filePaths.put(fileName, newName);
                    }
                }
            }
        }
        // 返回格式为[原文件名字, fastdfs 中的 key; 原文件名字, fastdfs 中的 key; ....]
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, String> entry : filePaths.entrySet()) {
            set.add(entry.getKey() + "," + entry.getValue());
            // 只会返回 fastdfs 的 keys, 不包含原文件名字
            //set.add(entry.getValue());
        }
        return new FileRes() {
            {
                setNamesAndKeys(StringUtils.join(set, ";"));
                // 只会返回 fastdfs 的 keys, 不包含原文件名字
                //setKeys(StringUtils.join(set, ","));
            }
        };
    }

    public FileRes fileUpload(String request) {
        Map<String,String> result_key = Base64Utils.setBase64ToImage(FileConfig.UPLOAD_PATH, request);

        Set<String> set = new HashSet<>();
        for (Map.Entry<String,String> entry : result_key.entrySet()
        ) {
            String filePath = fastDFSClientWrapper.uploadFile(new File(FileConfig.UPLOAD_PATH + entry.getValue()));
            set.add(filePath);
        }
        return new FileRes(){
            {
                setKeys(StringUtils.join(set,","));
            }
        };
    }

    @Override
    public FileRes fileUpload(String content, String fileType) {
        String result = fastDFSClientWrapper.uploadFile(content, fileType);
        return new FileRes(){
            {
                setNamesAndKeys(result);
            }
        };
    }

    public ResponseEntity<byte[]> download(String path, String fileOriginalName) {
        String[] paths = path.split(";");
        List<FileSlicesFdfsRes> list = getFileField(paths);
        if (list!=null && list.size()>0) {
            listSort(list);
            changeFilePath(list);
            int chunkSize = 0;
            for (int i = 0; i < list.size(); i++) {
                chunkSize += list.get(i).getFileSlicesSize();
            }
            byte[] allData = new byte[chunkSize];
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            int destPos = 0;
            for (FileSlicesFdfsRes item: list) {
                try {
                    // URLEncoder.encode(name, "UTF-8") 第一个参数是浏览器下载时文件的默认名字, 第二个是编码类型
                    headers.setContentDispositionFormData("attachment", URLEncoder.encode(fileOriginalName, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                byte[] data = fastDFSClientWrapper.downloadFile(item.getFileFdfsPath());
                int dataLength = data.length;
                // 将一个或者多个 byte[] 的内容拷贝到另一个 byte[] 中
                // data: 源数据; 0: 从源数据的什么位置开始拷贝; allData: 目标数据源;
                // destPos: 往目标数据源写数据时从哪个位置开始; dataLength: 往目标数据源里写多少字节的数据
                System.arraycopy(data, 0, allData, destPos, dataLength);
                destPos += dataLength;
            }
            return new ResponseEntity<>(allData, headers, HttpStatus.CREATED);
        }
        return null;
    }

    public ResponseEntity<byte[]> download(String filePath) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentDispositionFormData("attachment", URLEncoder.encode(filePath, ENCODING));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(fastDFSClientWrapper.downloadFile(filePath), headers, HttpStatus.CREATED);
    }

    public void delete(String filePath) {
        fastDFSClientWrapper.delete(filePath);
    }

    /**
     * @description: 根据 index 进行排序, 为了能够顺序整合文件
     * @param: [list]
     * @return: void
     * @author: liuzhichao 2020-08-31 16:30
     */
    public void listSort(List list) {
        Collections.sort(list, new Comparator<FileSlicesFdfsRes>() {
            @Override
            public int compare(FileSlicesFdfsRes o1, FileSlicesFdfsRes o2) {
                if (o1==null && o2==null) {
                    return 0;
                }
                if (o1==null) {
                    return -1;
                }
                if (o2==null) {
                    return 1;
                }
                if (o1.getFileSlicesIndex() > o2.getFileSlicesIndex()) {
                    return 1;
                } else if (o1.getFileSlicesIndex() < o2.getFileSlicesIndex()) {
                    return -1;
                }
                return 0;
            }
        });
    }

    /**
     * @description: 改变路径, 前端无法识别/ 所以转换成_ 在此给转换回来
     * @param: [list]
     * @return: void
     * @author: liuzhichao 2020-08-31 16:31
     */
    public void changeFilePath(List<FileSlicesFdfsRes> list) {
        if (list!=null && list.size()>0) {
            for (FileSlicesFdfsRes item: list) {
                String path = item.getFileFdfsPath();
                String prePath = path.substring(0, 17).replaceAll("_", "/");
                String subPath = path.substring(17);
                String newPath = prePath + subPath;
                item.setFileFdfsPath(newPath);
            }
        }
    }

    /**
     * @description:  获取文件属性
     * @param: [paths] "path, size; path, size;......"
     * @return: java.util.List<com.lzhch.common.file.dto.FileSlicesFdfsRes>
     * @author: liuzhichao 2020-08-31 16:35
     */
    public List<FileSlicesFdfsRes> getFileField(String[] paths){
        int size = paths.length;
        if (paths!=null && size>0) {
            // 传入 size 并不是初始化 list 的大小, 只是指定 list 的容纳能力 capacity
            // 所以这样初始化在 for 循环中 list.size() 拿到的永远都是 0  list 中有数据 size() 才不为 0
            //List<FileSlicesFdfsRes> list = new ArrayList<>(size);
            List<FileSlicesFdfsRes> list = new ArrayList<>();
            for (int i = 0; i<size; i++) {
                String[] fields = paths[i].split(",");
                FileSlicesFdfsRes item = new FileSlicesFdfsRes();
                item.setFileSlicesIndex(Integer.parseInt(fields[0]));
                item.setFileFdfsPath(fields[1]);
                item.setFileSlicesSize(Integer.parseInt(fields[2]));
                list.add(item);
            }
            return list;
        }
        return null;
    }

}
