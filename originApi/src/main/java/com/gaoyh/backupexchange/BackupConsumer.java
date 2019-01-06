package com.gaoyh.backupexchange;

import static com.gaoyh.backupexchange.BackupProducer.BACKUP_PRODUCER_EXCHANGE;
import static com.gaoyh.backupexchange.BackupProducer.MAIN_PRODUCER_EXCHANGE;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

/**
 * BackupConsumer
 *
 * @author gaoyh
 */
public class BackupConsumer {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        RabbitUtils.consumeMessage(connection, BACKUP_PRODUCER_EXCHANGE, Arrays.asList("#"), BuiltinExchangeType.FANOUT, true, false, null);
    }
}
