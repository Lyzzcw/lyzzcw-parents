package lyzzcw.work.component.common.http.constant.status;

import lombok.AllArgsConstructor;
import lyzzcw.work.component.domain.common.constant.IStatusCode;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 15:57
 * Description: No Description
 */
@AllArgsConstructor
public enum DeviceStatusCode implements IStatusCode {
    ERR_3000(3000,"设备id有误"),
    ERR_3001(3001,"设备名称格式错误"),
    ERR_3002(3002,"设备MAC地址无效")
    ;
    // 状态码
    private Integer status;

    // 状态码描述
    private String msg;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
