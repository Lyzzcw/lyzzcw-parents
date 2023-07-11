package lyzzcw.work.component.common.HttpUtils.constant.status;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 15:50
 * Description: 错误码的接口，所有定义错误码的类实现此接口
 */
public interface IStatusCode {
    int getStatus();
    String getMsg();
}
