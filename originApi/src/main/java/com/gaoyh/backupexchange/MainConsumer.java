package com.gaoyh.backupexchange;

import static com.gaoyh.backupexchange.BackupProducer.BACKUP_PRODUCER_EXCHANGE;
import static com.gaoyh.backupexchange.BackupProducer.MAIN_PRODUCER_EXCHANGE;
import static com.gaoyh.mandatory.MandatoryProducer.MANDATORY_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * MainConsumer
 *
 * @author gaoyh
 */
public class MainConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        HashMap<String, Object> map = new HashMap<>();
        map.put("alternate-exchange", BACKUP_PRODUCER_EXCHANGE);
        RabbitUtils.consumeMessage(connection, MAIN_PRODUCER_EXCHANGE, Arrays.asList("info"), BuiltinExchangeType.DIRECT,false,false,map);
    }
}
