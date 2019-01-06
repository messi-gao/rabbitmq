package com.gaoyh.producerconfirm;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * BatchConfirmProducer
 *
 * @author gaoyh
 */
public class BatchConfirmProducer {
    public static final String BATCH_CONFIRM_PRODUCER_EXCHANGE = "batch_confirm_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(BATCH_CONFIRM_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.confirmSelect();
        channel.addReturnListener(RabbitUtils::handleReturn);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(BATCH_CONFIRM_PRODUCER_EXCHANGE, routingKey, true, null, routingKey.getBytes());
        }
        channel.waitForConfirmsOrDie();
    }
}
