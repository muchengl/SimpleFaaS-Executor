package com.simplefaas.executor.utils;

import java.io.IOException;
import java.io.InputStream;

public class ShellUtil {

    public static String runSh(String[] cli){

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cli);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InputStream output=process.getInputStream();

        // 读取返回值
        String Final_String = null;
        try {
            Final_String = new String(output.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return Final_String;
    }

}
