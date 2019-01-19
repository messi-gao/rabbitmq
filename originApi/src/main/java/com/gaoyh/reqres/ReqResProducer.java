package com.gaoyh.reqres;

import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;


/**
 * @author yihe.gao
 */
public class ReqResProducer {
    public static final String REQ_RES_PRODUCER_EXCHANGE = "req_res_producer_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(REQ_RES_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.addReturnListener(RabbitUtils::handleReturn);

        String queue = channel.queueDeclare().getQueue();
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().replyTo(queue).messageId(UUID.randomUUID().toString()).build();
        /*声明了一个消费者*/
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received[" + envelope.getRoutingKey()
                        + "]" + message);
            }
        };
        /*消费者正式开始在指定队列上消费消息*/
        channel.basicConsume(queue, true, consumer);

        channel.basicPublish(REQ_RES_PRODUCER_EXCHANGE, "error", properties, "error".getBytes());
    }
}
