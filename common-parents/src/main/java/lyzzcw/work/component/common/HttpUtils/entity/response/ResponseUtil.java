package lyzzcw.work.component.common.HttpUtils.entity.response;



import lyzzcw.work.component.common.HttpUtils.constant.status.BaseStatusCode;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
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
    public static BaseResponseDto<Void> success() {
        return new BaseResponseDto(BaseStatusCode.SUCCESS);
    }

    /**
     * 根据Http状态码返回
     *
     * @return 基础的响应对象
     */
    public static BaseResponseDto<Void> successByHttpStatus() {
        return new BaseResponseDto(HttpStatus.OK);
    }

    /**
     * 根据自定义的状态码返回
     * 有响应数据的成功
     *
     * @param data 响应的数据
     * @param <T>  响应的数据类型
     * @return 基础的响应对象
     */
    public static <T> BaseResponseDto success(T data) {
        return new BaseResponseDto<T>(BaseStatusCode.SUCCESS, data);
    }

    /**
     * 根据http状态码返回
     *
     * @param data 响应的数据
     * @param <T>  响应的数据类型
     * @return 基础的响应对象
     */
    public static <T> BaseResponseDto successByHttpStatus(T data) {
        return new BaseResponseDto<T>(HttpStatus.OK, data);
    }

    /**
     * 没有响应数据的失败
     *
     * @param statusCode 状态码
     * @return
     */
    public static BaseResponseDto<Void> error(BaseStatusCode statusCode) {
        return new BaseResponseDto(statusCode);
    }

    /**
     * 有响应数据的失败
     *
     * @param statusCode 状态码
     * @param data       数据
     * @return
     */
    public static <T> BaseResponseDto error(BaseStatusCode statusCode, T data) {
        return new BaseResponseDto<T>(statusCode, data);
    }

    /**
     * 异常后的响应
     *
     * @param baseException 异常
     * @return
     */
    public static BaseResponseDto error(BaseException baseException) {
        return new BaseResponseDto(baseException);
    }


    /**
     * 异常后的响应
     * @param httpStatus
     * @param data
     * @return
     */
    public static <T> BaseResponseDto error(HttpStatus httpStatus, T data) {
        return new BaseResponseDto(httpStatus,data);
    }

    /**
     * 异常后的响应
     * @param httpStatus
     * @return
     */
    public static BaseResponseDto error(HttpStatus httpStatus) {
        return new BaseResponseDto(httpStatus);
    }
}
