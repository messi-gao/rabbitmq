package com.gaoyh.backupexchange;

import com.gaoyh.config.RabbitMqConfig;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * BackupProducer
 *
 * @author gaoyh
 */
public class BackupProducer {
    public static final String BACKUP_PRODUCER_EXCHANGE = "backup_producer_exchange";
    public static final String MAIN_PRODUCER_EXCHANGE = "main_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        HashMap<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", BACKUP_PRODUCER_EXCHANGE);
        channel.exchangeDeclare(MAIN_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT, false, false, map);
        channel.exchangeDeclare(BACKUP_PRODUCER_EXCHANGE, BuiltinExchangeType.FANOUT, true, false, null);
        for (String routingKey : RabbitMqConfig.ROUTING_KEY_LIST) {
            channel.basicPublish(MAIN_PRODUCER_EXCHANGE, routingKey, null, routingKey.getBytes());
        }
    }
}
