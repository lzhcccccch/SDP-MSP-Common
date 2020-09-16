package com.lzhch.common.file.controller;

import com.lzhch.common.file.api.FileApi;
import com.lzhch.common.file.dto.FileRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @packageName： com.lzhch.common.file.controller
 * @className: FileController
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 16:49
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileApi fileApi;

    @CrossOrigin
    @RequestMapping(value = "/SlicesUpload", method = RequestMethod.POST)
    public Map SlicesUpload (HttpServletRequest request) {
        return fileApi.SlicesUpload(request);
    }

    @CrossOrigin
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    public FileRes fileUpload(HttpServletRequest request) {
        return fileApi.fileUpload(request);
    }

    @CrossOrigin
    @RequestMapping(value = "/Base64Upload", method = RequestMethod.POST)
    public FileRes fileUpload(@RequestBody String request) {
        return fileApi.fileUpload(request);
    }

    @CrossOrigin
    @RequestMapping(value = "/SlicesDownload", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam("path") String path, @RequestParam("fileOriginalName") String fileOriginalName) {
        return fileApi.download(path, fileOriginalName);
    }

    @CrossOrigin
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@RequestParam("filePath") String filePath) {
        return fileApi.download(filePath);
    }

    @CrossOrigin
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestParam("filePath") String filePath) {
        fileApi.delete(filePath);
    }

}
