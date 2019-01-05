package com.gaoyh.exchange.direct;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.exchange.direct.DirectProducer.DIRECT_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.gaoyh.util.ThreadPoolUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * 多个消费者绑定一个队列 多线程
 *
 * @author gaoyh
 */
public class MultiCousumeInOneQueneConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, ExecutionException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        for (int i = 0; i < 3; i++) {
            threadPool.submit(() -> {
                try {
                    Channel channel = connection.createChannel();
                    channel.exchangeDeclare(DIRECT_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
                    channel.queueDeclare("queue", false, false, true, null);
                    for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
                        channel.queueBind("queue", DIRECT_PRODUCER_EXCHANGE, routingKey);
                    }
                    DefaultConsumer defaultConsumer = RabbitUtils.getAndHandleDefaultConsumer(channel);
                    channel.basicConsume("queue", defaultConsumer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
