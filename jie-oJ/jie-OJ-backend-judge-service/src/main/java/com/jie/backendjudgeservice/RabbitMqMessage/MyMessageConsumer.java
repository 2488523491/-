package com.jie.backendjudgeservice.RabbitMqMessage;

import com.jie.backendjudgeservice.judge.JudgeService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MyMessageConsumer {

    @Resource
    private JudgeService judgeService;

    //指定程序监听的消息队列和确认队列
    @SneakyThrows
    @RabbitListener(queues = {"code_queue"}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deiveryTag) {
        log.info("receiveMessage message = {}", message);
        long questionSubmitId = Long.parseLong(message);

        try {
            judgeService.doJudge(questionSubmitId);
            channel.basicAck(deiveryTag, false);
        } catch (Exception e) {
            channel.basicNack(deiveryTag, false,true                                                                                                                                                        );
        }

    }

}
