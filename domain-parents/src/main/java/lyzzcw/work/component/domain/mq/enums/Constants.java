package lyzzcw.work.component.domain.mq.enums;


/**
 * 常数
 *
 * @author lzy
 * @date 2023/12/14
 */
public class Constants {

    /*******************RocketMQ相关*******************/
    /**
     * 消息的key
     */
    public static final String MSG_KEY = "message";
    /**
     * 未读私聊消息队列
     */
    public final static String IM_MESSAGE_PRIVATE_QUEUE = "im_message_private";

    /**
     * 未读私聊消息空队列
     */
    public final static String IM_MESSAGE_PRIVATE_NULL_QUEUE = "im_null_private";

    /**
     * 监听私聊消息消费分组
     */
    public final static String IM_MESSAGE_PRIVATE_CONSUMER_GROUP = "im_message_private_consumer_group";
    /**
     * 未读群聊消息队列
     */
    public final static String IM_MESSAGE_GROUP_QUEUE = "im_message_group";

    /**
     * 未读群聊消息空队列
     */
    public final static String IM_MESSAGE_GROUP_NULL_QUEUE = "im_null_group";

    /**
     * 监听群聊消息消费分组
     */
    public final static String IM_MESSAGE_GROUP_CONSUMER_GROUP = "im_message_group_consumer_group";
    /**
     * 私聊消息发送结果队列
     */
    public final static String IM_RESULT_PRIVATE_QUEUE = "im_result_private";
    /**
     * 私聊消息结果消费分组
     */
    public final static String IM_RESULT_PRIVATE_CONSUMER_GROUP = "im_result_private_consumer_group";
    /**
     * 群聊消息发送结果队列
     */
    public final static String IM_RESULT_GROUP_QUEUE = "im_result_group";

    /**
     * 群聊消息结果消费分组
     */
    public final static String IM_RESULT_GROUP_CONSUMER_GROUP = "im_result_group_consumer_group";

}