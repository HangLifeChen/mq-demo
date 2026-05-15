package com.hzqaq.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendOrderWithCustomId(Order order) {
        // 自定义消息ID（例如：业务ID + 雪花算法ID）
        String customMessageId = order.getOrderId() + "_" + System.currentTimeMillis();
        
        // 使用 MessagePostProcessor 设置消息ID
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setMessageId(customMessageId);
            // 添加更多业务元数据
            message.getMessageProperties().setHeader("businessType", "ORDER");
            message.getMessageProperties().setHeader("businessId", order.getOrderId());
            message.getMessageProperties().setHeader("sendTime", System.currentTimeMillis());
            return message;
        };
        
        rabbitTemplate.convertAndSend(
            "order-exchange", 
            "order.created", 
            order, 
            messagePostProcessor
        );
        
        System.out.println("发送消息，ID: " + customMessageId);
    }
}