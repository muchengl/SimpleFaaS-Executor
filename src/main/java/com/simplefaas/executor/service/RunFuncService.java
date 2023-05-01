package com.simplefaas.executor.service;

import com.simplefaas.executor.ImageManager.ImageManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class RunFuncService implements RunFuncServiceInterface{


    @Autowired
    ConfigValue config;

    public String runFunc(String funcname,String data) {

        // 获取函数Image位置
        String localPath= null;
        try {
            localPath = ImageManager.getWasmImagePath(funcname,config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Image Path:"+localPath);
        System.out.println("Input Data:"+data);

        // 拼接运行指令
        String parameterArr[]=data.split(" ");

        String cli[] = new String[parameterArr.length+4];

        cli[0]=config.RuntimePath;
        cli[1]="--reactor";
        cli[2]=localPath; //函数Image位置
        cli[3]="run";

        for(int i=0;i<parameterArr.length;i++) {
            cli[i+4]=parameterArr[i];
        }


        // 运行WASM runtime
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

        System.out.println("Output Value:\n"+Final_String+"\n==================================================================\n");

        return Final_String;
    }

}
