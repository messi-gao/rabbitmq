package com.gaoyh.producerconfirm;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * AsyncConfirmProducer
 *
 * @author gaoyh
 */
public class AsyncConfirmProducer {
    public static final String ASYNC_CONFIRM_PRODUCER_EXCHANGE = "async_confirm_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(ASYNC_CONFIRM_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck deliveryTag：" + deliveryTag);
                System.out.println("handleAck multiple：" + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack deliveryTag：" + deliveryTag);
                System.out.println("handleNack multiple：" + multiple);
            }
        });
        channel.addReturnListener(RabbitUtils::handleReturn);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(ASYNC_CONFIRM_PRODUCER_EXCHANGE, routingKey, true, null, routingKey.getBytes());
        }
    }
}
