package lyzzcw.work.component.authentication.service.permission;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/1/12
 * Time: 16:21
 * Description: 自定义权限验证接口扩展
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        List<String> list = new ArrayList<String>();
        list.add("101");
        list.add("user*");
        list.add("user-add");
        list.add("user-delete");
        list.add("user-update");
        list.add("user-get");
        list.add("article-get");
        list.add("article-add");
        list.add("activity*");
        list.add("goods*");
        list.add("order*");
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本list仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        List<String> list = new ArrayList<String>();
        list.add("user");
        list.add("admin");
        list.add("super-admin");
        return list;
    }
}
