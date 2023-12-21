package lyzzcw.work.component.domain.mq.domain;

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0.0
 * @description 基础消息
 */
public class TopicMessage implements Serializable {

    private static final long serialVersionUID = -6489577536014906245L;
    /**
     * 消息的目的地，可以是消息的主题
     */
    private String destination;

    public TopicMessage() {
    }

    public TopicMessage(String destination) {
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
