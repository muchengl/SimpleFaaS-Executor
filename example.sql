create database faas;
use faas;

create table Task_status
(
    id              bigint auto_increment
        primary key,
    task_name       varchar(100) null comment '当前task的函数名称',
    task_func_id    bigint       null comment '当前task的函数id',
    task_status     int          null comment 'task运行状态',
    task_host_id    bigint       null comment 'task运行host的id',
    task_host_ip    varchar(30)  null comment 'task运行host的ip',
    task_begin_time datetime     null comment '任务开始运行时间',
    task_end_time   datetime     null comment '任务结束运行时间'
);

create table funcs
(
    id          bigint auto_increment
        primary key,
    func_name   varchar(100) null comment '函数名称',
    func_url    varchar(200) null comment '函数云端资源url',
    func_status int          null comment '函数是否被删除',
    constraint func_name
        unique (func_name)
);

create table instance_status
(
    id           bigint auto_increment
        primary key,
    host_ip      varchar(30) null,
    instance_num bigint      null comment '该主机上的运行实例数量',
    image_cache  text        null comment '当前实例缓存镜像情况'
);

