package com.hzqaq.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;


@Slf4j
@AllArgsConstructor
@Configuration
public class MqConfig {
    private final RabbitTemplate rabbitTemplate;
    // 构造方法执行完，并且依赖注入完成后才执行，自动被Spring调用
    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(@NonNull ReturnedMessage returned) {
                log.error("触发return callback,");
                log.info("exchange: {}", returned.getExchange());
                log.info("routingKey: {}", returned.getRoutingKey());
                log.info("message: {}", returned.getMessage());
                log.info("replyCode: {}", returned.getReplyCode());
                log.info("replyText: {}", returned.getReplyText());
            }
        });
    }
}