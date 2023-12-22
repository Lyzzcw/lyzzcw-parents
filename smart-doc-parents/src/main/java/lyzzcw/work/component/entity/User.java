package lyzzcw.work.component.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/5 9:46
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -3004624289691589697L;
    /**
     * 用户id
     */
    private Long id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 状态
     */
    private Integer status;
}
