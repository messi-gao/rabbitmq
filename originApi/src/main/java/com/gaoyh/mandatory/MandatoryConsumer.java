package com.gaoyh.mandatory;

import static com.gaoyh.exchange.topic.TopicProducer.TOPIC_PRODUCER_EXCHANGE;
import static com.gaoyh.mandatory.MandatoryProducer.MANDATORY_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * Mandatory
 *
 * @author gaoyh
 */
public class MandatoryConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, MANDATORY_PRODUCER_EXCHANGE, Arrays.asList("messi"), BuiltinExchangeType.DIRECT);
    }
}
