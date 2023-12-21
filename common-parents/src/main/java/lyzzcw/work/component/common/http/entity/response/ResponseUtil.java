package lyzzcw.work.component.common.http.entity.response;


import lyzzcw.work.component.domain.common.constant.BaseStatusCode;
import lyzzcw.work.component.domain.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 16:12
 * Description: 响应帮助类
 */
public class ResponseUtil {
    /**
     * 响应成功
     *
     * @return
     */
    public static BaseResponseVO<Void> success() {
        return new BaseResponseVO(BaseStatusCode.SUCCESS);
    }

    /**
     * 根据Http状态码返回
     *
     * @return 基础的响应对象
     */
    public static BaseResponseVO<Void> successByHttpStatus() {
        return new BaseResponseVO(HttpStatus.OK);
    }

    /**
     * 根据自定义的状态码返回
     * 有响应数据的成功
     *
     * @param data 响应的数据
     * @param <T>  响应的数据类型
     * @return 基础的响应对象
     */
    public static <T> BaseResponseVO success(T data) {
        return new BaseResponseVO<T>(BaseStatusCode.SUCCESS, data);
    }

    /**
     * 根据http状态码返回
     *
     * @param data 响应的数据
     * @param <T>  响应的数据类型
     * @return 基础的响应对象
     */
    public static <T> BaseResponseVO successByHttpStatus(T data) {
        return new BaseResponseVO<T>(HttpStatus.OK, data);
    }

    /**
     * 没有响应数据的失败
     *
     * @param statusCode 状态码
     * @return
     */
    public static BaseResponseVO<Void> error(BaseStatusCode statusCode) {
        return new BaseResponseVO(statusCode);
    }

    /**
     * 有响应数据的失败
     *
     * @param statusCode 状态码
     * @param data       数据
     * @return
     */
    public static <T> BaseResponseVO error(BaseStatusCode statusCode, T data) {
        return new BaseResponseVO<T>(statusCode, data);
    }

    /**
     * 异常后的响应
     *
     * @param baseException 异常
     * @return
     */
    public static BaseResponseVO error(BaseException baseException) {
        return new BaseResponseVO(baseException);
    }


    /**
     * 异常后的响应
     * @param httpStatus
     * @param data
     * @return
     */
    public static <T> BaseResponseVO error(HttpStatus httpStatus, T data) {
        return new BaseResponseVO(httpStatus,data);
    }

    /**
     * 异常后的响应
     * @param httpStatus
     * @return
     */
    public static BaseResponseVO error(HttpStatus httpStatus) {
        return new BaseResponseVO(httpStatus);
    }
}
