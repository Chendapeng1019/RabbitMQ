package rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Recv1
 * @Description
 * @Author chendapeng
 * @Date 2019/6/30
 **/
public class Recv1 {
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    private static final String QUEUE_NAME = "test_queue_topic1";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel =connection.createChannel();
        //声明交换机
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"goods.add");
        channel.basicQos(1);

        DeliverCallback deliverCallback =(consumerTag, delivery) ->{
            String message =new String(delivery.getBody(),"UTF-8");
            System.out.println("[1] Received "+message+"");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("[1] done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck,deliverCallback,consumerTag ->{});

    }
}
