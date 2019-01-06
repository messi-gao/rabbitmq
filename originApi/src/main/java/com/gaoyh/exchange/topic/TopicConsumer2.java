package com.gaoyh.exchange.topic;

import static com.gaoyh.exchange.topic.TopicProducer.TOPIC_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author gaoyh
 */
public class TopicConsumer2 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, TOPIC_PRODUCER_EXCHANGE, Arrays.asList("com.*"), BuiltinExchangeType.TOPIC);
    }
}
