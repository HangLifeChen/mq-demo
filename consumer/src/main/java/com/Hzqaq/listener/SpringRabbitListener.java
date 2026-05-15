package com.Hzqaq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
@Slf4j
public class SpringRabbitListener {
        // 利用RabbitListener来声明要监听的队列信息
    // 将来一旦监听的队列中有了消息，就会推送给当前服务，调用当前方法，处理消息。
    // 可以看到方法体中接收的就是消息体的内容
    @RabbitListener(queues = "yvhangchen1")
    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
        System.out.println("spring 消费者接收到消息：【" + msg + "】");
    }
    @RabbitListener(queues = "worker.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "worker.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(name = "hmall.direct"),
            key = {"red", "blue"}
    ))
    public void listenDirectQueue1(String msg){
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(name = "hmall.direct"),
            key = {"red", "yellow"}
    ))
    public void listenDirectQueue2(String msg){
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }

    @RabbitListener(
            containerFactory = "batchContainerFactory",
            queuesToDeclare = @Queue(
                    value = "batch.queue",    // 队列名称
                    durable = "false",          // 是否持久化
                    exclusive = "false",       // 是否独占
                    autoDelete = "false"       // 是否自动删除
            ))
    public void receiveBatch(List<Message> messages) {
        System.out.println("收到一批消息，数量: " + messages.size());
        // 遍历处理这一批消息
        for (Message msg : messages) {
            String body = new String(msg.getBody());
            System.out.println("处理消息内容: " + body+" id:"+msg.getMessageProperties().getMessageId());
            // 这里执行具体的业务逻辑
        }
        // 注意：如果没有异常抛出，Spring会自动提交这一批消息的ACK
    }

    @RabbitListener(queues = "batch.queue", concurrency = "3-5") // 3个核心线程，最多5个
    public void receiveSingle(Message message) {
        // 每条消息单独处理，但5个线程并行执行
        String body = new String(message.getBody());
        System.out.println(Thread.currentThread().getName() + " 处理: " + body);
    }

//    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueueMessage2(String msg) throws InterruptedException {
//        log.info("spring 消费者接收到消息：【{}】", msg);
//        throw new MessageConversionException("故意的");
//    }
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage2(String msg) throws InterruptedException {
        log.info("spring 消费者接收到消息：【{}】", msg);
        throw new RuntimeException("故意的");
    }


}