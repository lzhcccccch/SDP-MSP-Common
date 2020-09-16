package com.lzhch.file.service;

import com.lzhch.common.file.api.FileApi;
import com.lzhch.common.file.config.FileConfig;
import com.lzhch.common.file.dto.FileRes;
import com.lzhch.common.file.utils.Base64Utils;
import com.lzhch.common.file.utils.OperationFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @packageName： com.lzhch.file.service
 * @className: FileService
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-31 16:46
 */
@Service
public class FileService implements FileApi {

    public Map SlicesUpload(HttpServletRequest request) {
        return null;
    }

    public FileRes fileUpload(HttpServletRequest request) {
        //本地保存文件
        Map<String,String> result_key = new HashMap<String,String>();
        try {
            result_key = OperationFileUtil.multiFileUpload(request, FileConfig.UPLOAD_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResult(result_key);
    }

    public FileRes fileUpload(String request) {
        Map<String,String> result_key = Base64Utils.setBase64ToImage(FileConfig.UPLOAD_PATH,request);
        return getResult(result_key);
    }

    public FileRes fileUpload(String content, String fileType) {
        return null;
    }

    public ResponseEntity<byte[]> download(String path, String fileOriginalName) {
        return null;
    }

    public ResponseEntity<byte[]> download(String filePath) {
        return OperationFileUtil.download(FileConfig.UPLOAD_PATH + filePath);
    }

    public void delete(String filePath) {
        OperationFileUtil.deleteFile(filePath);
    }

    public FileRes getResult(Map<String, String> map) {
        Set<String> set = new HashSet<>();
        for (Map.Entry<String,String> entry : map.entrySet()
        ) {
            set.add(entry.getValue());
        }
        return new FileRes(){
            {
                setKeys(StringUtils.join(set, ","));
            }
        };
    }

}
