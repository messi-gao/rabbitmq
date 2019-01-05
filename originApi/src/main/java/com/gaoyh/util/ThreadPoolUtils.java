package com.gaoyh.util;

import com.gaoyh.config.RabbitMqConfig;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * 工具类
 *
 * @author gaoyh
 */
public class ThreadPoolUtils {

    public static ThreadPoolExecutor getThreadPool() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d").build();
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(availableProcessors,
                availableProcessors, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(availableProcessors), threadFactory);

    }
}
