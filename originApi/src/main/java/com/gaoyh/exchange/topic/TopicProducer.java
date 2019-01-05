package com.gaoyh.exchange.topic;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Fanout消费者
 *
 * @author gaoyh
 */
public class TopicProducer {
    public static final String TOPIC_PRODUCER_EXCHANGE = "topic_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(TOPIC_PRODUCER_EXCHANGE, BuiltinExchangeType.TOPIC);
        for (String routingKey : RabbitMqConfig.TOPIC_ROUTING_KEY_LIST) {
            channel.basicPublish(TOPIC_PRODUCER_EXCHANGE, routingKey, null, routingKey.getBytes());
        }
        channel.close();
        connection.close();
    }
}
