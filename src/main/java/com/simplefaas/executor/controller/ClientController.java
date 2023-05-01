package com.simplefaas.executor.controller;

import com.simplefaas.executor.service.RunFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {

    @Autowired
    RunFuncService runner;



    @RequestMapping("/run")
    public String run(String funcname,String data) {
        return runner.runFunc(funcname,data);
    }
}
