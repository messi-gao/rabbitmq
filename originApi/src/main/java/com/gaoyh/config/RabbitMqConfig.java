package com.gaoyh.config;

import java.util.Arrays;
import java.util.List;

public class RabbitMqConfig {
    public static final String HOST = "192.168.1.7";
    public static final int PORT = 5672;
    public static final String USER_NAME = "mark";
    public static final String PASSWORD = "123456";
    public static final String EXCHANGE = "exchange_demo";
    public static final String QUEUE = "queue_demo";
    public static final String ROUTING_KEY = "routing_key_demo";
    public static final String VIRTUAL_HOST = "enjoyedu";
    public static final List<String> ROUTING_KEY_LIST = Arrays.asList("info","warn","error");
    public static final List<String> TOPIC_ROUTING_KEY_LIST = Arrays.asList("com.messi","com.messi.basa.10","com.10");
}
