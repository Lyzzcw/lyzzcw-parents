package lyzzcw.work.component.domain.mq.domain;


import lyzzcw.work.component.domain.mq.enums.MQSendTypeEnum;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/1/21
 * Time: 14:10
 * Description: No Description
 */
public abstract class AbstractMessage {
    /** MQ名称 **/
    public abstract String getMQName();

    /** MQ 类型 **/
    public abstract MQSendTypeEnum getMQType();

    /** 构造MQ消息体 String类型 **/
    public abstract String toMessage();
}
