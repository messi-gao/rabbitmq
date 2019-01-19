package com.gaoyh.durable;

import static com.gaoyh.durable.DurableProducer.DURABLE_PRODUCER_EXCHANGE;
import static com.gaoyh.reqres.ReqResProducer.REQ_RES_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoyh
 */
public class DurableConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "durable_queue";
        channel.queueDeclare(queueName, true, false, true, null);

        channel.exchangeDeclare(DURABLE_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT,true);
        RabbitUtils.bindRouteKeys(DURABLE_PRODUCER_EXCHANGE, Arrays.asList("error"), channel, queueName);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
            }
        };
        channel.basicConsume(queueName, true, defaultConsumer);
    }
}
