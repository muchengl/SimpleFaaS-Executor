package com.simplefaas.executor;

import com.alibaba.fastjson.JSON;
import com.simplefaas.executor.ImageManager.ImageManager;
import com.simplefaas.executor.config.BeanContext;
import com.simplefaas.executor.controller.ClientController;
import com.simplefaas.executor.pojo.ServerStatus;
import com.simplefaas.executor.service.ConfigValue;
import com.simplefaas.executor.utils.HttpUtil;
import com.simplefaas.executor.utils.IPUtil;
import org.apache.http.client.HttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class SinpleFaaSExecutorApplication {

    public static void main(String[] args) {

        SpringApplication.run(SinpleFaaSExecutorApplication.class, args);


        ClientController.taskNum=new AtomicInteger(0);

        String ip= IPUtil.getLocalIp();
        ConfigValue configValue= BeanContext.getBean(ConfigValue.class);
        String registryHost= configValue.registryHost;
        System.out.println(registryHost+" "+ip);


        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){

                    // 上报状态
                    String url="http://"+registryHost+":8081/servers/reg";

                    Map<String,String> map=new HashMap<>();
                    map.put("host_ip",ip);
                    map.put("task_num",ClientController.taskNum.toString());
                    map.put("image_cache", ImageManager.getImageList());


                    try {
                        HttpUtil.requestForHttp(url,map);
                        System.out.println("Successful reporting status to:"+registryHost+"  Msg:"+JSON.toJSONString(map));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Fail reporting status to:"+registryHost+"  Msg:"+JSON.toJSONString(map));
//
                    }


                    try {
                        Thread.sleep(configValue.aliveTime*1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();


    }

}
