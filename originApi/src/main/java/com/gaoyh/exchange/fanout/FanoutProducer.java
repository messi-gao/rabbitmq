package com.gaoyh.exchange.fanout;

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
public class FanoutProducer {
    public static final String FANOUT_PRODUCER_EXCHANGE = "fanout_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(FANOUT_PRODUCER_EXCHANGE, BuiltinExchangeType.FANOUT);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(FANOUT_PRODUCER_EXCHANGE, routingKey, null, routingKey.getBytes());
        }
        channel.close();
        connection.close();
    }
}
