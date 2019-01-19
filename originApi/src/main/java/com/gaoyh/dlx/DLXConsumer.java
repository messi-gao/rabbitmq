package com.gaoyh.dlx;

import static com.gaoyh.consumerreject.RejectProducer.REJECT_PRODUCER_EXCHANGE;
import static com.gaoyh.dlx.DLXProducer.DLX_EXCHANGE;
import static com.gaoyh.dlx.DLXProducer.DLX_ROUTING_KEY;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoyh
 */
public class DLXConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "dlx_queue";
        channel.queueDeclare(queueName, false, false, true, null);

        channel.exchangeDeclare(DLX_EXCHANGE,BuiltinExchangeType.DIRECT);
        RabbitUtils.bindRouteKeys(DLX_EXCHANGE, Arrays.asList(DLX_ROUTING_KEY), channel, queueName);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(Thread.currentThread().getName() + "线程，接收到消息：" + new String(body));
            }
        };
        channel.basicConsume(queueName, false, defaultConsumer);
    }
}
