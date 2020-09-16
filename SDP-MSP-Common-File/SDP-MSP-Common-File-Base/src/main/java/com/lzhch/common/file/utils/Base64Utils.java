package com.lzhch.common.file.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @packageName： com.lzhch.common.file.utils
 * @className: Base64Utils
 * @description: TODO
 * @version: v1.0
 * @author: liuzhichao
 * @date: 2020-08-25 21:56
 */
public class Base64Utils {

    /**
     * 将文件转成base64 字符串
     * @param path 文件路径
     * @return  *
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);;
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }

    /**
     * 将base64字符解码保存文件
     * @param base64Code
     * @param filsPath
     * @throws Exception
     */
//    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
//        byte[] buffer = new BASE64Decoder().decodeBuffer(base64Code);
//        FileOutputStream out = new FileOutputStream(targetPath);
//        out.write(buffer);
//        out.close();
//
//    }
    public static String decoderBase64File(String base64Code, String filsPath,String suffix) { // 对字节数组字符串进行Base64解码并生成图片
        //生成文件最终地址
        filsPath += UUIDUtil.getUUID()+suffix;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(base64Code);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            FileOutputStream out = new FileOutputStream(filsPath);
            out.write(b);
            out.close();

            return filsPath;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将base64字符保存文本文件
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code, String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }


    /**
     * 本地图片转换成base64字符串
     * @param imgFile	图片本地路径
     * @return
     */
    public static String ImageToBase64ByLocal(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);

            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();

        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * 在线图片转换成base64字符串
     *
     * @param imgURL	图片线上路径
     * @return
     */
    public static String ImageToBase64ByOnline(String imgURL) {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(imgURL);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data.toByteArray());
    }

    /**
     * base64字符串转换成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     */
    public static String Base64ToImage(String imgStr,String imgFilePath,String suffix) { // 对字节数组字符串进行Base64解码并生成图片
        //生成最终图片地址
        imgFilePath += UUIDUtil.getUUID()+suffix;

        BASE64Decoder decoder = new BASE64Decoder();

        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();

            return imgFilePath;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * base64字符串转换成图片
     * @param imgStr		base64字符串
     * @param imgFilePath	图片存放路径
     * @return
     */
    public static InputStream Base64ToInputStream(String imgStr,String imgFilePath) { // 对字节数组字符串进行Base64解码并生成图片
//        if (StringUtil.isEmpty(imgStr)) // 图像数据为空
//            return false;
        imgFilePath += UUIDUtil.getUUID();

        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }

            InputStream input = new ByteArrayInputStream(b);
            return input;
        } catch (Exception e) {
            return null;
        }
    }

    public static Map<String,String> setBase64ToImage(String imgFilePath, String[] base64Str) {
        Map<String,String> map = new HashMap();
        String suffix = "";
        String images = ".jpg.jpeg.png";
        for (String str:base64Str) {
            if(str.indexOf("base64,")!=-1){
                String[] buf = str.split("base64,");
                str=buf[1];
                String su = buf[0].split("/")[1];
                suffix="."+su.substring(0,su.length()-1);
            }
            String imagesUrl = Base64Utils.Base64ToImage(str,imgFilePath,suffix);
            System.out.println(imagesUrl.substring(imagesUrl.lastIndexOf("/")+1));
            String filename = imagesUrl.substring(imagesUrl.lastIndexOf("/")+1);
            map.put(filename,filename);
        }
        return map;
    }

    public static Map<String,String> setBase64ToImage(String imgFilePath,String base64Str) {
        Map<String,String> map = new HashMap();
        String suffix = "";
        String imgStr ="";
        String fileUrl="";

        if(base64Str.indexOf("base64,")!=-1){
            String[] buf = base64Str.split("base64,");
            imgStr=buf[1];
            String su = buf[0].split("/")[1];

            if(su.indexOf("sheet")!=-1){
                suffix = ".xlsx";
            }else if(su.indexOf("document")!=-1){
                suffix = ".docx";
            }else{
                suffix="."+su.substring(0,su.length()-1);
            }
        }
        String images = ".jpg.jpeg.png";
        if(images.indexOf(suffix)!=-1){
            fileUrl = Base64Utils.Base64ToImage(imgStr,imgFilePath,suffix);
        }else{

            fileUrl = Base64Utils.decoderBase64File(imgStr,imgFilePath,suffix);
        }
        System.out.println(fileUrl.substring(fileUrl.lastIndexOf("/")+1));
        String filename = fileUrl.substring(fileUrl.lastIndexOf("/")+1);
        map.put(filename,filename);
        return map;
    }

}
