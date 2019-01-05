package com.gaoyh.exchange.direct;

import com.gaoyh.config.RabbitMqConfig;
import static com.gaoyh.exchange.direct.DirectProducer.DIRECT_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.gaoyh.util.ThreadPoolUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

/**
 * 多个信道绑定一个交换器 多线程
 *
 * @author gaoyh
 */
public class MultiChannelBindExchangeWithMultiThreadConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, ExecutionException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        for (int i = 0; i < 3; i++) {
            threadPool.submit(() -> {
                try {
                    RabbitUtils.consumeMessage(connection, DIRECT_PRODUCER_EXCHANGE, RabbitMqConfig.ROUTING_KEY_LIST, BuiltinExchangeType.DIRECT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
