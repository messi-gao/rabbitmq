package com.gaoyh.helloworld;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.gaoyh.helloworld.config.RabbitMqConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;


/**
 * @author yihe.gao
 */
public class RabbitProducer {


    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMqConfig.HOST);
        connectionFactory.setPort(RabbitMqConfig.PORT);
        connectionFactory.setVirtualHost(RabbitMqConfig.VIRTUAL_HOST);
        connectionFactory.setUsername(RabbitMqConfig.USER_NAME);
        connectionFactory.setPassword(RabbitMqConfig.PASSWORD);

        // 创建链接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 创建一个 type="direct" 、持久化的、非自动删除的交换器
        channel.exchangeDeclare(RabbitMqConfig.EXCHANGE, "direct", true, false, null);
        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(RabbitMqConfig.QUEUE, true, false, false, null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(RabbitMqConfig.QUEUE, RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY);
        // 发送一条持久化的消息: hello world !
        String message = "Hello World";
        channel.basicPublish(RabbitMqConfig.EXCHANGE, RabbitMqConfig.ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        // 关闭资源
        channel.close();
        connection.close();
    }
}
