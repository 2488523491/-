package com.jie.backendjudgeservice;

import com.jie.backendjudgeservice.RabbitMqMessage.InitRabbitMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;


// todo 如需开启 Redis，须移除 exclude 中的内容
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.jie")
@EnableDiscoveryClient
@EnableFeignClients(basePackages= {"com.jie.backendserviceclient.service"})
public class jiebakckendJudgeServiceApplication {
    public static void main(String[] args) {
        //初始化消息队列,每次启动都要启动消息队列
        InitRabbitMq.doInit();
        SpringApplication.run(jiebakckendJudgeServiceApplication.class, args);
    }

}
