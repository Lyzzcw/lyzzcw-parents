package lyzzcw.work.component.entity;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/1/26
 * Time: 14:30
 * Description: No Description
 */
// spring方式加载,生成的实现类上面会自动添加一个@Component注解，可以通过Spring的 @Autowired方式进行注入
   /* 我们在编译时会报 java: No property named "numberOfSeats" exists in source parameter(s). Did you mean "null"? 错误
            经过查阅资料发现 mapstruct-processor 和 Lombok 的版本需要统一一下：
            mapstruct-processor：1.2.0.Final ， Lombok：1.16.14*/
//@Mapper(componentModel = "spring")
@Mapper
public interface UserConverter {
    // default方式加载
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * 将DTO转VO
     * source 源数据
     * target 目标数据
     * @param userDTO
     * @return
     */
    User userDTO2User(UserDTO userDTO);

    UserVO user2UserVO(User user);
    List<UserVO> user2UserVOList(List<User> userList);
}
