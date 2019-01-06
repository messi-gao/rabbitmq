package com.gaoyh.producerconfirm;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.producerconfirm.AsyncConfirmProducer.ASYNC_CONFIRM_PRODUCER_EXCHANGE;
import static com.gaoyh.producerconfirm.BatchConfirmProducer.BATCH_CONFIRM_PRODUCER_EXCHANGE;
import static com.gaoyh.producerconfirm.ConfirmProducer.CONFIRM_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ConfirmConsumer
 *
 * @author gaoyh
 */
public class ConfirmConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, CONFIRM_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, BuiltinExchangeType.DIRECT);
        RabbitUtils.consumeMessage(connection, ASYNC_CONFIRM_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, BuiltinExchangeType.DIRECT);
        RabbitUtils.consumeMessage(connection, BATCH_CONFIRM_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, BuiltinExchangeType.DIRECT);
    }
}
