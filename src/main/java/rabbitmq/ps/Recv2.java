package rabbitmq.ps;

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
public class Recv2 {
    private static final String QUEUE_NAME = "test_queue_fanout_sms";
    private static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel =connection.createChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
        channel.basicQos(1); //保证一次只发一次

        DeliverCallback deliverCallback =(consumerTag, delivery) ->{
            String message =new String(delivery.getBody(),"UTF-8");
            System.out.println("[2] Received "+message+"");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                System.out.println("[2] done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            }
        };
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck,deliverCallback,consumerTag ->{});
    }
}
