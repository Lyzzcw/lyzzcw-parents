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
public class UserResponseVO {
    // 简单视图，只返回最基数的属性
    public interface UserResponseSimpleVOView extends BaseResponseVO.BaseResponseVOView {};

    // 详情视图，返回详细的属性参数
    public interface UserResponseDetailVOView extends UserResponseSimpleVOView {};


    /**
     * 用户名
     */
    @JsonView(UserResponseSimpleVOView.class)
    public String userName;

    /**
     * 年龄
     */
    @JsonView(UserResponseSimpleVOView.class)
    private Integer age;

    /**
     * 性别
     */
    @JsonView(UserResponseSimpleVOView.class)
    private Integer gender;

    /**
     * 邮箱
     */
    @JsonView(UserResponseSimpleVOView.class)
    private String email;

    /**
     * 电话号码
     */
    @JsonView(UserResponseSimpleVOView.class)
    private String phoneNum;

    /**
     * 修改人
     */
    @JsonView(UserResponseDetailVOView.class)
    private String optUser;
}
