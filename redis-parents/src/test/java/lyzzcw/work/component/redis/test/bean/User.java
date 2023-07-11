package lyzzcw.work.component.redis.test.bean;

import cn.hutool.json.JSONUtil;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 9:01
 * Description: No Description
 */
public class User {
    private Long id;
    private String name;
    public User() {
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
}
