package com.lzhch.file.oss.domain;

import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.lzhch.common.file.config.FileConfig;
import com.lzhch.file.oss.utils.OssClient;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @packageName： com.lzhch.file.oss.domain
 * @className: OssDo
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-09-01 10:55
 */
public class OssDo {

    public Set<String> upload(Map<String,String> result_key){
        Set<String> s = new HashSet<>();
        //阿里云OSS保存
        for (Map.Entry<String,String> entry:result_key.entrySet()
        ) {
            //直接上传OSS
            CompleteMultipartUploadResult completeMultipartUploadResult = OssClient.upload(entry.getValue(), FileConfig.UPLOAD_PATH + entry.getValue());
            s.add(completeMultipartUploadResult.getKey());
        }
        return s;
    }

}
