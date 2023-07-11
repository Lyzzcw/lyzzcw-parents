package lyzzcw.work.component.common.HttpUtils.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lyzzcw.work.component.common.HttpUtils.constant.status.BaseStatusCode;
import lyzzcw.work.component.common.HttpUtils.constant.status.IStatusCode;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZonedDateTime;


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
     * url 请求路径
     */
    private String url;

    /**
     * errorStackTrack 返回错误信息堆栈
     */
    private String errorStackTrack;

    /**
     * 请求结果生成的时间戳
     */
    private Instant time;

    /**
     * 构造函数
     */
    public Result() {
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 实体类
     * @param success
     * @param data
     * @param message
     * @param code
     */
    public Result(boolean success, T data, String message, Integer code) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.code = code;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param iStatusCode
     */
    public Result(IStatusCode iStatusCode) {
        this.code = iStatusCode.getStatus();
        this.message = iStatusCode.getMsg();
        this.time = ZonedDateTime.now().toInstant();
    }


    public Result(boolean success, String message, Integer code) {
        Result.of(success, null, message, code);
    }

    public Result(boolean success, IStatusCode iStatusCode) {
        Result.of(success, null, iStatusCode.getMsg(), iStatusCode.getStatus());
    }

    /**
     * @param success
     * @param iStatusCode
     * @param data
     */
    public Result(boolean success,IStatusCode iStatusCode, T data) {
        Result.of(success, data, iStatusCode.getMsg(), iStatusCode.getStatus());
    }


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
     *
     * @Title: success
     * @param @return
     * @return Result<T>
     * @throws
     */
    public static <T> Result<T> ok() {
        return Result.ok(BaseStatusCode.SUCCESS);
    }

    /**
     *
     * @Title: success
     * @param @param data
     * @param @return
     * @return Result<T>
     * @throws
     */
    public static <T> Result<T> ok(T data) {
        return Result.of(true,data,
                BaseStatusCode.SUCCESS.getMsg(),BaseStatusCode.SUCCESS.getStatus());
    }

    /**
     * 添加返回信息
     * @param iStatusCode
     * @return
     */
    public static <T> Result<T> ok(IStatusCode iStatusCode) {
        return Result.of(true, null, iStatusCode.getMsg(),
                iStatusCode.getStatus());
    }

    /**
     * 添加返回信息
     * @param message
     * @return
     */
    public static <T> Result<T> ok(String message) {
        return Result.of(true, null, message,
                BaseStatusCode.SUCCESS.getStatus());
    }

    /**
     * 添加返回信息
     * @param message
     * @param code
     * @return
     */
    public static <T> Result<T> ok(String message, Integer code) {
        return Result.of(true, null, message, code);
    }

    /**
     * 添加返回信息
     * @param data
     * @param message
     * @return
     */
    public static <T> Result<T> ok(T data, String message) {
        return Result.of(true, data, message,
                BaseStatusCode.SUCCESS.getStatus());
    }

    /**
     *
     * @Title: 添加返回信息
     * @param data
     * @param @return
     * @return ExecuteResult<T>
     * @throws
     */
    public static <T> Result<T> fail(T data) {
        return Result.of(false, data, BaseStatusCode.FAIL.getMsg(), BaseStatusCode.FAIL.getStatus());
    }

    /**
     * @Title: failure
     * @param @return
     * @param iStatusCode
     * @return
     */
    public static <T> Result<T> fail(IStatusCode iStatusCode) {
        return Result.of(false, null, iStatusCode.getMsg(), iStatusCode.getStatus());
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param iStatusCode
     * @param data
     * @return Result
     */
    public static <T> Result<T> fail(IStatusCode iStatusCode, T data) {
        return Result.of(false, data, iStatusCode.getMsg(), iStatusCode.getStatus());
    }

    /**
     * @Title: failure
     * @param @return
     * @param message
     * @return
     */
    public static <T> Result<T> fail(String message) {
        return Result.of(false, null, message, BaseStatusCode.FAIL.getStatus());
    }


    /**
     *
     * @param message
     * @param errorcode
     * @return
     */
    public static <T> Result<T> fail(String message, Integer errorcode) {
        return  Result.of(false,null,message,errorcode);
    }

    /**
     *
     * @Title: failure
     * @param @return
     * @return Result<T>
     * @throws
     */
    public static <T> Result<T> fail() {
        return Result.fail(BaseStatusCode.FAIL);
    }

    /**
     *
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static <T> Result<T> fail(T data, String message, Integer code) {
        return Result.of(false,data,message,code);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     * @param iStatusCode
     */
    public static void throwFail(IStatusCode iStatusCode) {
        throw new BaseException(iStatusCode);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     * @param message
     */
    public static void throwFail(String message) {
        throw new BaseException(BaseStatusCode.FAIL,message);
    }

    /**
     * 直接抛出失败异常，抛出 code 码
     * @param code
     * @param message
     */
    public static void throwFail(Integer code, String message) {
        throw new BaseException(message);
    }

    /**
     * 构造一个异常的API返回
     *
     * @param <T> {@link BaseException} 的子类
     * @return Result
     */
    public static <T extends BaseException> Result<T> ofException(T t) {
        return of(false, null, t.getMessage(), t.getError());
    }

    /**
     * 构造一个异常的API返回
     *
     * @param <T> {@link BaseException} 的子类
     * @param t   编码
     * @return Result
     */
    public static <T extends BaseException> Result<T> ofException(T t, Integer code) {
        return of(false, null, t.getMessage(), code);
    }

}
