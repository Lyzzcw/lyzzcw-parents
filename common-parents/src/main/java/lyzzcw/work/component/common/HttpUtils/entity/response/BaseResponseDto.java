package lyzzcw.work.component.common.HttpUtils.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lyzzcw.work.component.common.HttpUtils.constant.status.IStatusCode;
import lyzzcw.work.component.common.HttpUtils.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 16:01
 * Description: 基础的响应对象
 */
@Data
public class BaseResponseDto<T> {
    /**
     * 响应数据最外层的视图 也是所有响应视图的父类
     */
    public interface BaseResponseDtoView {
    }

    /**
     * 状态码
     */
    @JsonView(BaseResponseDtoView.class)
    private Integer status;

    /**
     * 状态描述
     */
    @JsonView(BaseResponseDtoView.class)
    private String msg;

    /**
     * 响应数据
     */
    @JsonView(BaseResponseDtoView.class)
    private T data;

    /**
     * 只有状态码的响应
     *
     * @param statusCode
     */
    public BaseResponseDto(IStatusCode statusCode) {
        if (null != statusCode) {
            this.status = statusCode.getStatus();
            this.msg = statusCode.getMsg();
        }
    }

    /**
     * 有状态码且有参数的响应
     *
     * @param statusCode
     * @param data
     */
    public BaseResponseDto(IStatusCode statusCode, T data) {
        if (null != statusCode) {
            this.status = statusCode.getStatus();
            this.msg = statusCode.getMsg();
        }
        if (null != data) {
            this.data = data;
        }
    }

    /**
     * 根据HttpStatus响应
     *
     * @param httpStatus http请求状态码
     */
    public BaseResponseDto(HttpStatus httpStatus) {
        if (null != httpStatus) {
            this.status = httpStatus.value();
            this.msg = httpStatus.getReasonPhrase();
        }
    }

    /**
     * 根据http状态码返回 并返回额外返回数据
     *
     * @param httpStatus http状态码
     * @param data       数据
     */
    public BaseResponseDto(HttpStatus httpStatus, T data) {
        if (null != httpStatus) {
            this.status = httpStatus.value();
            this.msg = httpStatus.getReasonPhrase();
        }
        if (null != data) {
            this.data = data;
        }
    }

    /**
     * 根据异常响应错误码
     *
     * @param baseException 异常对象
     */
    public BaseResponseDto(BaseException baseException) {
        if (null != baseException) {
            this.status = baseException.getError();
            this.msg = baseException.getMsg();
            this.data = (T) baseException.getData();
        }
    }
}
