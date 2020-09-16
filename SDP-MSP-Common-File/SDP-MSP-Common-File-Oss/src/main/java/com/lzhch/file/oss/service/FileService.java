package com.lzhch.file.oss.service;

import com.lzhch.common.file.api.FileApi;
import com.lzhch.common.file.config.FileConfig;
import com.lzhch.common.file.dto.FileRes;
import com.lzhch.common.file.utils.Base64Utils;
import com.lzhch.common.file.utils.OperationFileUtil;
import com.lzhch.file.oss.domain.OssDo;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @packageName： com.lzhch.file.oss.service
 * @className: FileService
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-01 10:57
 */
@Service
public class FileService implements FileApi {

    @Override
    public Map SlicesUpload(HttpServletRequest request) {
        return null;
    }

    @Override
    public FileRes fileUpload(HttpServletRequest request) {
        //本地保存文件
        Map<String,String> result_key = new HashMap<String,String>();
        try {
            result_key = OperationFileUtil.multiFileUpload(request, FileConfig.UPLOAD_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        OssDo ossDo = new OssDo();
        Set<String> s = ossDo.upload(result_key);
        return new FileRes(){
            {
                setKeys(StringUtils.join(s,","));
            }
        };
    }

    @Override
    public FileRes fileUpload(String request) {
        Map<String,String> result_key = Base64Utils.setBase64ToImage(FileConfig.UPLOAD_PATH,request);
//        Map<String,String> result_key = Base64Utils.setBase64ToImage(FileConfig.UPLOAD_PATH,request.getBase64Str());

        OssDo ossDo = new OssDo();
        Set<String> s = ossDo.upload(result_key);
        return new FileRes(){
            {
                setKeys(StringUtils.join(s,","));
            }
        };
    }

    @Override
    public FileRes fileUpload(String content, String fileType) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> download(String path, String fileOriginalName) {
        return null;
    }

    @Override
    public ResponseEntity<byte[]> download(String filePath) {
        return OperationFileUtil.download(FileConfig.UPLOAD_PATH + filePath);
    }

    @Override
    public void delete(String filePath) {

    }

}
