package lyzzcw.work.component.common.http.entity.response;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/9
 * Time: 16:17
 * Description: 用户响应请求
 */
@Data
public class UserResponseDto {
    // 简单视图，只返回最基数的属性
    public interface UserResponseSimpleDtoView extends BaseResponseDto.BaseResponseDtoView {};

    // 详情视图，返回详细的属性参数
    public interface UserResponseDetailDtoView extends UserResponseSimpleDtoView {};


    /**
     * 用户名
     */
    @JsonView(UserResponseSimpleDtoView.class)
    public String userName;

    /**
     * 年龄
     */
    @JsonView(UserResponseSimpleDtoView.class)
    private Integer age;

    /**
     * 性别
     */
    @JsonView(UserResponseSimpleDtoView.class)
    private Integer gender;

    /**
     * 邮箱
     */
    @JsonView(UserResponseSimpleDtoView.class)
    private String email;

    /**
     * 电话号码
     */
    @JsonView(UserResponseSimpleDtoView.class)
    private String phoneNum;

    /**
     * 修改人
     */
    @JsonView(UserResponseDetailDtoView.class)
    private String optUser;
}
