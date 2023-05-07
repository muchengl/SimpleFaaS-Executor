package com.simplefaas.executor;

import com.simplefaas.executor.config.BeanContext;
import com.simplefaas.executor.service.ConfigValue;
import com.simplefaas.executor.utils.IPUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class SinpleFaaSExecutorApplication {

    public static void main(String[] args) {

        SpringApplication.run(SinpleFaaSExecutorApplication.class, args);


        String ip= IPUtil.getLocalIp();

        ConfigValue configValue= BeanContext.getBean(ConfigValue.class);
        String registryHost= configValue.registryHost;

        System.out.println(registryHost+" "+ip);


    }

}
