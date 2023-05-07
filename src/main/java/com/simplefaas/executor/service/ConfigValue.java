package com.simplefaas.executor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConfigValue {

    // value.OSSAccessKey=
    // value.OSSAccessKeySecret=
    // value.OSSArn=

    @Value("${value.OSSAccessKey}")
    public String OSSAccessKey;

    @Value("${value.OSSAccessKeySecret}")
    public String OSSAccessKeySecret;

    @Value("${value.OSSArn}")
    public String OSSArn;


    @Value("${value.OSSRegion}")
    public String OSSRegion;

    @Value("${value.RuntimePath}")
    public String RuntimePath;

    @Value("${value.ImageStorgePath}")
    public String ImageStorgePath;


    @Value("${value.registryHost}")
    public String registryHost;






}
