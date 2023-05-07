package com.simplefaas.executor.ImageManager;

import com.alibaba.fastjson.JSON;
import com.simplefaas.executor.service.ConfigValue;
import com.simplefaas.executor.utils.OSSUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageManager {


    public static String getImageList(){
        ImageLruCache imageCache=ImageLruCache.getInstance();

        return JSON.toJSONString(imageCache.getNodeList());
    }

    public static String getWasmImagePath(String funcName, ConfigValue config) throws IOException {

        // Get from Cache
        ImageLruCache imageCache=ImageLruCache.getInstance();
        if(!imageCache.get(funcName).equals("")){
            System.out.println("Get From Cache:"+funcName);
            return imageCache.get(funcName);
        }



        String urlStr= OSSUtil.readImage(funcName,config);

        String localPath=config.ImageStorgePath;


        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");


        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(localPath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+ File.separator+funcName+".wasm");

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }



        System.out.println("Info:"+funcName+" Download Success");

        imageCache.put(funcName,file.getPath());

        return file.getPath();


    }


    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }



}
