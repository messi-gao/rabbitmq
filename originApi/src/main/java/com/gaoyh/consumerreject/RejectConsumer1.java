package com.gaoyh.consumerreject;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.consumerqos.QosProducer.QOS_PRODUCER_EXCHANGE;
import static com.gaoyh.consumerreject.RejectProducer.REJECT_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author gaoyh
 */
public class RejectConsumer1 {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        String queueName = "focuserror";
        channel.queueDeclare(queueName,false,false,false,null);
        RabbitUtils.bindRouteKeys(REJECT_PRODUCER_EXCHANGE, Arrays.asList("error"), channel, queueName);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(Thread.currentThread().getName() + "线程，接收到消息：" + new String(body));
                    throw new RuntimeException("出错了");
                }catch (Exception e){
                    channel.basicReject(envelope.getDeliveryTag(),true);
                }
            }
        };
        channel.basicConsume(queueName, false, defaultConsumer);
    }
}
