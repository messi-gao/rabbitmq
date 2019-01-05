package com.gaoyh.exchange.direct;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.exchange.direct.DirectProducer.DIRECT_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 一个队列绑定一个交换器多个路由键
 *
 * @author gaoyh
 */
public class OneQueueBindMultiRouteKeyConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, DIRECT_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, BuiltinExchangeType.DIRECT);
    }
}
