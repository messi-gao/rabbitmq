package com.gaoyh.producerconfirm;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * ConfirmProducer
 *
 * @author gaoyh
 */
public class ConfirmProducer {
    public static final String CONFIRM_PRODUCER_EXCHANGE = "confirm_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(CONFIRM_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.confirmSelect();
        channel.addReturnListener(RabbitUtils::handleReturn);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(CONFIRM_PRODUCER_EXCHANGE, routingKey, true, null, routingKey.getBytes());
            if (channel.waitForConfirms()) {
                System.out.println("发送消息成功");
            }else {
                System.out.println("发送消息失败");
            }
        }
        channel.close();
        connection.close();
    }
}
