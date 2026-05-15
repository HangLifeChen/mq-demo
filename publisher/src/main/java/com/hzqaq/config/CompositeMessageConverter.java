package com.hzqaq.config;

import org.jspecify.annotations.NullMarked;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.*;

public class CompositeMessageConverter implements MessageConverter {

        private final JacksonJsonMessageConverter jsonConverter = new JacksonJsonMessageConverter();
        private final SimpleMessageConverter simpleConverter = new SimpleMessageConverter();

        @Override
        @NullMarked
        public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
            // 如果对象是String且看起来像纯文本，用simple转换器
            if (object instanceof String && !((String) object).trim().startsWith("{")) {
                return simpleConverter.toMessage(object, messageProperties);
            }
            // 其他情况用JSON转换器
            return jsonConverter.toMessage(object, messageProperties);
        }
        
        @Override
        @NullMarked
        public Object fromMessage(Message message) throws MessageConversionException {
            // 尝试用JSON解析，失败则用简单转换器
            try {
                return jsonConverter.fromMessage(message);
            } catch (Exception e) {
                return simpleConverter.fromMessage(message);
            }
        }
        public void setCreateMessageIds(boolean b){
            jsonConverter.setCreateMessageIds(b);
            simpleConverter.setCreateMessageIds(b);
        }
    }