package com.simplefaas.executor.controller;

import com.simplefaas.executor.service.RunFuncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class ClientController {

    @Autowired
    RunFuncService runner;

    public static AtomicInteger taskNum;


    @RequestMapping("/run")
    public String run(String funcname,String data) {

        System.out.println("Task: "+funcname+" "+data);

        taskNum.getAndIncrement();

        String res=runner.runFunc(funcname,data);

        taskNum.getAndDecrement();

        return res;
    }
}
