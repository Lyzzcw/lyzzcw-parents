package lyzzcw.work.component.mq.service;


import lyzzcw.work.component.domain.mq.domain.TopicMessage;
import org.apache.rocketmq.client.producer.TransactionSendResult;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 8:44
 * Description: 发送消息服务
 */
public interface MessageSenderService {
    /**
     * 发送消息
     * @param message 发送的消息
     */
    boolean send(TopicMessage message);

    /**
     * 发送事务消息，主要是RocketMQ
     * @param message 事务消息
     * @param arg 其他参数
     * @return 返回事务发送结果
     */
    default TransactionSendResult sendMessageInTransaction(TopicMessage message, Object arg){
        return null;
    }
}
