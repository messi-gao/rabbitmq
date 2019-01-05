package com.gaoyh.exchange.direct;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 直接消费者
 *
 * @author gaoyh
 */
public class DirectProducer {
    public static final String DIRECT_PRODUCER_EXCHANGE = "direct_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DIRECT_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(DIRECT_PRODUCER_EXCHANGE, routingKey, null, routingKey.getBytes());
        }
        channel.close();
        connection.close();
    }
}
