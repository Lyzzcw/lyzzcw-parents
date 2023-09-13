package lyzzcw.work.component.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 18:43
 * Description: No Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * token
     */
    private String token;
}
