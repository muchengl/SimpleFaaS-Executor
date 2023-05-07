package com.simplefaas.executor.controller;

import com.simplefaas.executor.utils.IPUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController {


    @RequestMapping("/init")
    public String init(String ip){

        // 获取本地ip
        // curl ifconfig.me


        return "ok";
    }
}
