package com.gaoyh.consumerreject;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * @author yihe.gao
 */
public class RejectProducer {
    public static final String REJECT_PRODUCER_EXCHANGE = "reject_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(REJECT_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.addReturnListener(RabbitUtils::handleReturn);
        for (int i = 0; i < 5; i++) {
            channel.basicPublish(REJECT_PRODUCER_EXCHANGE, "error", null, ("error" + i).getBytes());
        }
    }
}
