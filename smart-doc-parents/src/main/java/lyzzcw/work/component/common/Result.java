package lyzzcw.work.component.common;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;


/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 16:01
 * Description: 返回类
 */
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Result<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * success
     */
    private boolean success;

    /**
     * T <>
     */
    private T data;

    /**
     * message 返回信息
     */
    private String message;

    /**
     * code 错误码
     */
    private Integer code;

    /**
     * 构造一个自定义的API返回
     *
     * @param success 是否成功
     * @param code    状态码
     * @param message 返回内容
     * @param data    返回数据
     * @return Result
     */
    public static <T> Result<T> of(boolean success, T data, String message, Integer code) {
        return new Result<T>(success, data, message, code);
    }


    /**
     * 添加返回信息
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data) {
        return Result.of(true, data, "success", 200);
    }

}
