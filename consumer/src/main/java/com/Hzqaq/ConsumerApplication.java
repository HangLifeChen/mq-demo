package com.Hzqaq;

import com.Hzqaq.config.CompositeMessageConverter;
import org.springframework.amqp.support.converter.DefaultJacksonJavaTypeMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

//    @Bean
//    public MessageConverter messageConverter() {
//        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
//        DefaultJacksonJavaTypeMapper typeMapper = new DefaultJacksonJavaTypeMapper();
//        typeMapper.setTrustedPackages("*");
//        converter.setJavaTypeMapper(typeMapper);
//        return converter;
//    }

    @Bean
    public MessageConverter messageConverter() {
        // 方式A：使用SimpleMessageConverter（支持String, byte[], Serializable）
        // return new SimpleMessageConverter();

        // 方式B：自定义转换器，根据内容类型自动选择
        CompositeMessageConverter converter=new CompositeMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;

    }
}
