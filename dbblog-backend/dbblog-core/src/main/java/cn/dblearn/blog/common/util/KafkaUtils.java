package cn.dblearn.blog.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;

/**
 * RabbitMqUtils
 *
 * @author bobbi
 * @date 2019/03/16 22:08
 * @email 571002217@qq.com
 * @description
 */
@Component
@Slf4j
public class KafkaUtils {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送到指定topic
     * @param topic
     * @param object
     */
    public void send(String topic, Object object) {

        /*
         * 这里的ListenableFuture类是spring对java原生Future的扩展增强,是一个泛型接口,用于监听异步方法的回调
         * 而对于kafka send 方法返回值而言，这里的泛型所代表的实际类型就是 SendResult<K, V>,而这里K,V的泛型实际上
         * 被用于ProducerRecord<K, V> producerRecord,即生产者发送消息的key,value 类型
         */
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, object);
        future.addCallback(
                o -> log.info("消息发送成功,{}", o.toString()), throwable -> log.info("消息发送失败,{}" + throwable.getMessage())
        );

    }
}
