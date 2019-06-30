package rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import rabbitmq.util.ConnectionUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Send
 * @Description
 * @Author chendapeng
 * @Date 2019/6/30
 **/
public class Send {
    private static final String EXCHANGE_NAME = "test_exchange_topic";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        Channel channel =connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");

        String msg="商品 ......";
        channel.basicPublish(EXCHANGE_NAME,"goods.delete",null,msg.getBytes());

        System.out.println("Send: "+msg);

        channel.close();
        connection.close();
    }
}
