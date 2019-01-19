package com.gaoyh.dlx;

import static com.gaoyh.consumerreject.RejectProducer.REJECT_PRODUCER_EXCHANGE;
import static com.gaoyh.dlx.DLXProducer.*;
import com.gaoyh.util.RabbitUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

/**
 * @author gaoyh
 */
public class RejectConsumer {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitUtils.getConnection();
        Channel channel = connection.createChannel();

        String queueName = "focuserror";
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("x-dead-letter-exchange", DLX_EXCHANGE);
        objectObjectHashMap.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);

        channel.queueDeclare(queueName,false,false,true,objectObjectHashMap);
        channel.exchangeDeclare(DLX_PRODUCER_EXCHANGE, BuiltinExchangeType.DIRECT);
        RabbitUtils.bindRouteKeys(DLX_PRODUCER_EXCHANGE, Arrays.asList("error"), channel, queueName);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    System.out.println(Thread.currentThread().getName() + "线程，接收到消息：" + new String(body));
                    throw new RuntimeException("出错了");
                }catch (Exception e){
                    channel.basicReject(envelope.getDeliveryTag(),false);
                }
            }
        };
        channel.basicConsume(queueName, false, defaultConsumer);
    }
}
