package com.gaoyh.transcaction;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoyh
 */
public class TransactionProducer {
    public static final String MANDATORY_PRODUCER_EXCHANGE = "transaction_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        try {
            channel.txSelect();
            channel.exchangeDeclare(MANDATORY_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.basicPublish(MANDATORY_PRODUCER_EXCHANGE, "routingKey", null, "transaction".getBytes());
            int result = 1 / 0;
            channel.txCommit();
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        }
    }
}
