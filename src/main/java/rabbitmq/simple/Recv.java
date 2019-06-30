package rabbitmq.simple;

import com.rabbitmq.client.*;
import rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Recv
 * @Description 消费者获取消息
 * @Author chendapeng
 * @Date 2019/6/29
 **/
public class Recv {

    private static final String QUEUE_NAME="test_simple_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection=ConnectionUtils.getConnection();

        //创建频道
        Channel channel = connection.createChannel();

         //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback =(consumerTag,deliver) ->{
            String message = new String(deliver.getBody(),"UTF-8");
            System.out.println("[X] Recevied "+ message);
        };

        //监听队列
        channel.basicConsume(QUEUE_NAME,true,deliverCallback, consumerTag->{});

    }
}
