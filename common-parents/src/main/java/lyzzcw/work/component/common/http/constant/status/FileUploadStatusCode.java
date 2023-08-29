package lyzzcw.work.component.common.http.constant.status;

import lombok.AllArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 15:57
 * Description: No Description
 */
@AllArgsConstructor
public enum FileUploadStatusCode implements IStatusCode {
    /**
     * 系统未知异常
     */
    FAIL(10000, "请求数据提交失败"),

    /**
     * 操作成功
     */
    SUCCESS(200, "请求数据提交成功"),

    /**
     * 文件已存在
     */
    EXIST(101,"文件已存在"),

    /**
     * 该文件没有上传过
     */
    NOUPLOAD(102,"该文件没有上传过"),

    /**
     * 该文件上传了一部分
     */
    PARTUPLOAD(103,"该文件上传了一部分")
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
