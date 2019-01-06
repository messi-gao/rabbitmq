package com.gaoyh.util;

import com.gaoyh.config.RabbitMqConfig;
import com.rabbitmq.client.*;
import org.apache.commons.collections.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 工具类
 *
 * @author gaoyh
 */
public class RabbitUtils {
    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername(RabbitMqConfig.USER_NAME);
        connectionFactory.setPassword(RabbitMqConfig.PASSWORD);
        connectionFactory.setVirtualHost(RabbitMqConfig.VIRTUAL_HOST);
        return connectionFactory.newConnection(new Address[]{new Address(RabbitMqConfig.HOST, RabbitMqConfig.PORT)});
    }

    public static void consumeMessage(Connection connection, String exchange, List<String> routeKeyList,
                                      BuiltinExchangeType builtinExchangeType) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange, builtinExchangeType);
        String queue = channel.queueDeclare().getQueue();
        if (CollectionUtils.isNotEmpty(routeKeyList)) {
            for (String routingKey : routeKeyList) {
                channel.queueBind(queue, exchange, routingKey);
            }
        }
        DefaultConsumer defaultConsumer = getAndHandleDefaultConsumer(channel);
        channel.basicConsume(queue, defaultConsumer);
    }

    public static void consumeMessage(Connection connection, String exchange, List<String> routeKeyList,
                                      BuiltinExchangeType builtinExchangeType, boolean durable, boolean autoDelete,
                                      Map<String, Object> arguments) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(exchange, builtinExchangeType.getType(), durable, autoDelete, arguments);
        String queue = channel.queueDeclare().getQueue();
        if (CollectionUtils.isNotEmpty(routeKeyList)) {
            for (String routingKey : routeKeyList) {
                channel.queueBind(queue, exchange, routingKey);
            }
        }
        DefaultConsumer defaultConsumer = getAndHandleDefaultConsumer(channel);
        channel.basicConsume(queue, defaultConsumer);
    }

    public static DefaultConsumer getAndHandleDefaultConsumer(Channel channel) {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(Thread.currentThread().getName() + "线程，接收到消息：" + new String(body));
            }
        };
    }

    public static void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) {
        System.out.println("Body:" + new String(body));
        System.out.println("Exchange:" + exchange);
        System.out.println("ReplyText:" + replyText);
        System.out.println("RoutingKey:" + routingKey);
        System.out.println("ReplyCode:" + replyCode);
    }
}
