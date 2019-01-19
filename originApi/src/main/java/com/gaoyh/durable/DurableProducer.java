package com.gaoyh.durable;

import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


/**
 * @author yihe.gao
 */
public class DurableProducer {
    public static final String DURABLE_PRODUCER_EXCHANGE = "durable_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DURABLE_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT,true);
        channel.addReturnListener(RabbitUtils::handleReturn);
        channel.basicPublish(DURABLE_PRODUCER_EXCHANGE, "error",true, MessageProperties.PERSISTENT_TEXT_PLAIN, "error".getBytes());

    }
}
