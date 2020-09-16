package com.lzhch.common.file.dto;

import lombok.Data;

import java.util.Date;

/**
 * @packageName： com.lzhch.common.file.dto
 * @className: FileSlicesFdfsRes
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 16:02
 */
@Data
public class FileSlicesFdfsRes {

    private int id;

    private String uuid;

    private int fileSlicesIndex;

    private int fileSlicesSize;

    private String fileFdfsPath;

    private String fileFullPath;

    private int isDelete;

    private Date createTime;

    private Date updateTime;

}
