package lyzzcw.work.component.common.HttpUtils.entity.request;


import lombok.Data;
import lyzzcw.work.component.common.HttpUtils.validate.checkModel.CaseCheck;
import lyzzcw.work.component.common.HttpUtils.validate.checkModel.CaseMode;
import lyzzcw.work.component.common.HttpUtils.validate.group.UserRequestDtoAddValidate;
import lyzzcw.work.component.common.HttpUtils.validate.group.UserRequestDtoSimpleValidate;
import lyzzcw.work.component.common.HttpUtils.validate.group.UserRequestDtoUpdateValidate;

import javax.validation.constraints.*;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2021/12/8
 * Time: 10:27
 * Description: user请求参数实例
 */
@Data
public class UserRequestDto {
    @NotBlank(message = "姓名不能为空",groups = UserRequestDtoSimpleValidate.class)
    @CaseCheck(value = CaseMode.LOWER,message = "用户名必须为小写",groups = UserRequestDtoSimpleValidate.class)
    private String userName;

    @NotBlank(message = "密码不能为空",groups = UserRequestDtoAddValidate.class)
    private String password;

    @NotNull(message = "年龄不能为空",groups = UserRequestDtoSimpleValidate.class)
    @Min(value = 0,message = "年龄不能小于0岁",groups = UserRequestDtoSimpleValidate.class)
    @Max(value = 120,message = "年龄不能大于120岁",groups = UserRequestDtoSimpleValidate.class)
    private Integer age;

    private String address;

    @NotBlank(message = "手机号码不能为空",groups = UserRequestDtoUpdateValidate.class)
    @Pattern(regexp = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$",
            message = "号码格式不正确!",groups = UserRequestDtoUpdateValidate.class)
    private String phoneNum;
}
