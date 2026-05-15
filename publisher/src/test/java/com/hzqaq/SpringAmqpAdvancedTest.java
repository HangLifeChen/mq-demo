package com.hzqaq;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@SpringBootTest
public class SpringAmqpAdvancedTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Test
    void contextLoads() {
    }
//    @Test
//    void testPublisherConfirm() throws ExecutionException, InterruptedException, TimeoutException {
//        // 1.创建CorrelationData
//        CorrelationData cd = new CorrelationData();
//        // 2.给Future添加回调（使用CompletableFuture API，替代已移除的ListenableFutureCallback）
//        cd.getFuture().whenComplete((result, ex) -> {
//            if (ex != null) {
//                // 2.1.Future发生异常时的处理逻辑，基本不会触发
//                log.error("send message fail", ex);
//                return;
//            }
//            // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
//            if (result.ack()) { // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
//                log.info("发送消息成功，收到 ack!");
//            } else { // result.getReason()，String类型，返回nack时的异常描述
//                log.error("发送消息失败，收到 nack, reason : {}", result.reason());
//            }
//        });
//        // 3.发送消息到一个【存在的exchange】但使用【不存在的routing key】
//        // 注意：exchange必须存在！否则只会收到nack，不会触发return
//        rabbitTemplate.convertAndSend("hmall.direct", "non_exist_routing_key", "hello", cd);
//        // 4.阻塞等待confirm回执返回
//        cd.getFuture().get(5, TimeUnit.SECONDS);
//        // 5.检查是否收到return（return在confirm之前到达，会设置到cd中）
//        Thread.sleep(1000); // 等待return回调处理
//        if (cd.getReturned() != null) {
//            log.info("收到 return: replyCode={}, replyText={}",
//                Objects.requireNonNull(cd.getReturned()).getReplyCode(), Objects.requireNonNull(cd.getReturned()).getReplyText());
//        } else {
//            log.warn("未收到 return（消息已成功路由到队列，或exchange不存在）");
//        }
//    }

    @Test
    void testConsumerRetry() throws ExecutionException, InterruptedException, TimeoutException {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData();
        // 2.给Future添加回调（使用CompletableFuture API，替代已移除的ListenableFutureCallback）
        cd.getFuture().whenComplete((result, ex) -> {
            if (ex != null) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
                return;
            }
            // 2.2.Future接收到回执的处理逻辑，参数中的result就是回执内容
            if (result.ack()) { // result.isAck()，boolean类型，true代表ack回执，false 代表 nack回执
                log.info("发送消息成功，收到 ack!");
            } else { // result.getReason()，String类型，返回nack时的异常描述
                log.error("发送消息失败，收到 nack, reason : {}", result.reason());
            }
        });
        // 3.发送消息到一个【存在的exchange】但使用【不存在的routing key】
        // 注意：exchange必须存在！否则只会收到nack，不会触发return
        rabbitTemplate.convertAndSend("somplie.queue", Optional.of("hello"), cd);
        // 4.阻塞等待confirm回执返回
        cd.getFuture().get(5, TimeUnit.SECONDS);
        // 5.检查是否收到return（return在confirm之前到达，会设置到cd中）
        Thread.sleep(1000); // 等待return回调处理
        if (cd.getReturned() != null) {
            log.info("收到 return: replyCode={}, replyText={}",
                    Objects.requireNonNull(cd.getReturned()).getReplyCode(), Objects.requireNonNull(cd.getReturned()).getReplyText());
        } else {
            log.warn("未收到 return（消息已成功路由到队列，或exchange不存在）");
        }
    }
}
