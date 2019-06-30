package rabbitmq.workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Send
 * @Description |-----c1
 * P------Queue---- |
 * |-----c2
 * @Author chendapeng
 * @Date 2019/6/29
 **/
public class Send {
    private static final String QUEUE_NAME = "test_work_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();
        //获取channel
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //每个消费者确认消息之前，消息队列不发送下一个到消费者，一次只处理一个消息
        //限制发送给同一个消费者 不超过一条消息
        int perfetchCount = 1;
        channel.basicQos(perfetchCount);

        for (int i = 0; i < 50; i++) {
            String msg = "hello " + i;
            System.out.println("send:" + msg);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            Thread.sleep(i * 20);
        }
        channel.close();
        connection.close();
    }
}
