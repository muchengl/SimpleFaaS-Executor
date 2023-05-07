package com.simplefaas.executor.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerStatus {

//    private long id;
    private String host_ip;
    private long task_num;
    private String image_cache;



    public void update(ServerStatus newStatus){
        this.host_ip=newStatus.getHost_ip();
        this.task_num=newStatus.getTask_num();
        this.image_cache=newStatus.getImage_cache();


    }


}
