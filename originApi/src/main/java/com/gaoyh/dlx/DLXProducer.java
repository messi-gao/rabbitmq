package com.gaoyh.dlx;

import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;


/**
 * @author yihe.gao
 */
public class DLXProducer {
    public static final String DLX_PRODUCER_EXCHANGE = "dlx_producer_exchange";
    public static final String DLX_EXCHANGE = "dlx_exchange";
    public static final String DLX_ROUTING_KEY = "dlx_routing_key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(DLX_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.addReturnListener(RabbitUtils::handleReturn);

        for (int i = 0; i < 5; i++) {
            channel.basicPublish(DLX_PRODUCER_EXCHANGE, "error", null, ("error" + i).getBytes());
        }
    }
}
