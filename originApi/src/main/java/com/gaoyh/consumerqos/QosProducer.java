package com.gaoyh.consumerqos;

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
public class QosProducer {
    public static final String QOS_PRODUCER_EXCHANGE = "qos_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(QOS_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.addReturnListener(RabbitUtils::handleReturn);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(QOS_PRODUCER_EXCHANGE, routingKey,  null, routingKey.getBytes());
        }
    }
}
