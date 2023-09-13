package lyzzcw.work.component.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lyzzcw.work.component.common.Result;
import lyzzcw.work.component.entity.User;
import lyzzcw.work.component.entity.UserConverter;
import lyzzcw.work.component.entity.UserDTO;
import lyzzcw.work.component.entity.UserVO;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * 用户登录
 * @author: lzy
 * Date: 2023/7/5 9:46
 * Time: 10:42
 * Description:
 */
@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    /**
     * 系统测试
     * @param userName
     * @return
     */
    @GetMapping( "/get")
    public Result<User> getUser(String userName){

        log.info("{} -> {}","当前登录账户",userName);

        return Result.ok(new User());
    }

    /**
     * 登录
     * @param userDTO
     * @return
     */
    @PostMapping( "/doLogin")
    public Result<UserVO> login(@RequestBody UserDTO userDTO){
        User user = UserConverter.INSTANCE.userDTO2User(userDTO);
        User loginUser = new User();
        Assert.notNull(loginUser,"用户不存在");

        UserVO result = UserConverter.INSTANCE.user2UserVO(loginUser);
        result.setToken("Bearer " + "123456");
        return Result.ok(result);
    }
}
