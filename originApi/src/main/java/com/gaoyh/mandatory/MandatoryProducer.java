package com.gaoyh.mandatory;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Fanout消费者
 *
 * @author gaoyh
 */
public class MandatoryProducer {
    public static final String MANDATORY_PRODUCER_EXCHANGE = "mandatory_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(MANDATORY_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.addReturnListener((replyCode, replyText, exchange, routingKey, properties, body) -> {
            System.out.println("Body:" + new String(body));
            System.out.println("Exchange:" + exchange);
            System.out.println("ReplyText:" + replyText);
            System.out.println("RoutingKey:" + routingKey);
            System.out.println("ReplyCode:" + replyCode);
        });
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(MANDATORY_PRODUCER_EXCHANGE, routingKey, true, null, routingKey.getBytes());
        }
    }
}
