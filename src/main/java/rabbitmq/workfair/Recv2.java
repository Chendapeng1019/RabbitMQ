package rabbitmq.workfair;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Recv
 * @Description
 * @Author chendapeng
 * @Date 2019/6/29
 **/
public class Recv2 {

    private static final String QUEUE_NAME ="test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取连接
        Connection connection = ConnectionUtils.getConnection();

        //获取channel
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);

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

//    private static void doWork(String task) throws InterruptedException {
//        for(char ch: task.toCharArray()){
//            if(ch == '.'){
//               Thread.sleep(1000);
//            }
//        }
//    }
}
