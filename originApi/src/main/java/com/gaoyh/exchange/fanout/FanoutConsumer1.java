package com.gaoyh.exchange.fanout;

import static com.gaoyh.exchange.fanout.FanoutProducer.FANOUT_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * 一个队列绑定一个交换器多个路由键
 *
 * @author gaoyh
 */
public class FanoutConsumer1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, FANOUT_PRODUCER_EXCHANGE, Arrays.asList("fanout"), BuiltinExchangeType.FANOUT);
    }
}
