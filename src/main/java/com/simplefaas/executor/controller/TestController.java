package com.simplefaas.executor.controller;


import com.simplefaas.executor.utils.OSSUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/getImage")
    public String getImage(String filename){
        //String url=OSSUtil.readImage(filename);

        return "ok";
    }


}
