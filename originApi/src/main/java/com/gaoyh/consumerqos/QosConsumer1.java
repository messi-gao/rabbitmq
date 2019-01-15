package com.gaoyh.consumerqos;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.consumerqos.QosProducer.QOS_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoyh
 */
public class QosConsumer1 {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        AtomicInteger messageCount = new AtomicInteger(0);
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        String queue = channel.queueDeclare().getQueue();
        RabbitUtils.bindRouteKeys(QOS_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, channel, queue);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.println(Thread.currentThread().getName() + "线程，接收到消息：" + new String(body));
            }
        };
        channel.basicQos(2);
        DefaultConsumer qosConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                int i = messageCount.incrementAndGet();
                if (i == 2) {
                    channel.basicAck(envelope.getDeliveryTag(), true);
                    System.out.println("qos线程，消息确认，DeliveryTag：" + envelope.getDeliveryTag());
                }
                System.out.println(Thread.currentThread().getName() + "qos线程，接收到消息：" + new String(body));
            }
        };
        channel.basicConsume(queue, false, defaultConsumer);
        channel.basicConsume(queue, false, qosConsumer);
    }
}
