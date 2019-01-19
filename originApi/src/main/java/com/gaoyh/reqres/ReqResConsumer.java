package com.gaoyh.reqres;

import static com.gaoyh.reqres.ReqResProducer.REQ_RES_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoyh
 */
public class ReqResConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "reqres_queue";
        channel.queueDeclare(queueName, false, false, true, null);

        channel.exchangeDeclare(REQ_RES_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        RabbitUtils.bindRouteKeys(REQ_RES_PRODUCER_EXCHANGE, Arrays.asList("error"), channel, queueName);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received["+envelope.getRoutingKey()
                        +"]"+message);
                AMQP.BasicProperties respProp
                        = new AMQP.BasicProperties.Builder()
                        .replyTo(properties.getReplyTo())
                        .correlationId(properties.getMessageId())
                        .build();

                channel.basicPublish("", respProp.getReplyTo() ,
                        respProp ,
                        ("Hi,"+message).getBytes("UTF-8"));
            }
        };
        channel.basicConsume(queueName, true, defaultConsumer);
    }
}
