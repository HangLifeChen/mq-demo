package com.Hzqaq.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchRabbitConfig {

    @Bean("batchContainerFactory")
    public SimpleRabbitListenerContainerFactory batchContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);  // 使用自定义转换器
        factory.setConsumerBatchEnabled(true);  // 启用批量消费
        factory.setBatchSize(10);               // 每批最多10条
        factory.setBatchListener(true);         // 标记为批量监听器
        return factory;
    }
}
