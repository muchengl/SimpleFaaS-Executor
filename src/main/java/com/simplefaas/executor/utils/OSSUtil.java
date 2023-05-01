package com.simplefaas.executor.utils;


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.simplefaas.executor.service.ConfigValue;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;

public class OSSUtil {


    public static boolean writeImage(ByteArrayInputStream arr,String filename){

        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tJQN5axw4dTPUMQdVHM";
        String accessKeySecret = "jwvFeIpqwraKC8qaqboxnjOSLWr8Ws";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);



        // 填写Bucket名称，例如examplebucket。
        String bucketName = "wepets";
        // 填写文件名。文件名包含路径，不包含Bucket名称。例如exampledir/exampleobject.txt。
        String objectName = "userHead/"+filename;


        ossClient.deleteObject(bucketName, objectName);

        ossClient.putObject(bucketName, objectName,arr);

        // 关闭OSSClient。
        ossClient.shutdown();

        return true;
    }

    public static String readImage(String filename, ConfigValue config){
        // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "oss-cn-beijing.aliyuncs.com";


        String sts=getSecurityToken(config);
        if(sts.equals("--------")) return"";

        // 从STS服务获取的临时访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = sts.split("----")[1];
        String accessKeySecret = sts.split("----")[2];
        // 从STS服务获取的安全令牌（SecurityToken）。
        String securityToken = sts.split("----")[0];


        // 填写Bucket名称，例如examplebucket。
        String bucketName = "simple-faas";
        // 填写Object完整路径，例如exampleobject.txt。Object完整路径中不能包含Bucket名称。
        String objectName = filename+".wasm";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, securityToken);

        // 设置签名URL过期时间为3600秒（1小时）。
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
        //System.out.println(url);
        // 关闭OSSClient。
        ossClient.shutdown();

        return url.toString();
    }




    public static String getSecurityToken(ConfigValue config){
        //构建一个阿里云客户端，用于发起请求。
        //构建阿里云客户端时需要设置AccessKey ID和AccessKey Secret。
        DefaultProfile profile = DefaultProfile.getProfile(config.OSSRegion,
                config.OSSAccessKey,
                config.OSSAccessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        //构造请求，设置参数。关于参数含义和设置方法，请参见API参考。
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setRegionId(config.OSSRegion);
        request.setRoleArn(config.OSSArn);
        request.setRoleSessionName("user");

        String s1="";
        String s2="";
        String s3="";
        //发起请求，并得到响应。
        try {
            AssumeRoleResponse response = client.getAcsResponse(request);
            //System.out.println(new Gson().toJson(response));

            //System.out.println(response.getCredentials().getSecurityToken());
            s1=response.getCredentials().getSecurityToken();
            s2=response.getCredentials().getAccessKeyId();
            s3=response.getCredentials().getAccessKeySecret();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
//                    System.out.println("ErrCode:" + e.getErrCode());
//                    System.out.println("ErrMsg:" + e.getErrMsg());
//                    System.out.println("RequestId:" + e.getRequestId());
        } catch (com.aliyuncs.exceptions.ClientException e) {
            e.printStackTrace();
        }

        return s1+"----"+s2+"----"+s3;


    }





}
