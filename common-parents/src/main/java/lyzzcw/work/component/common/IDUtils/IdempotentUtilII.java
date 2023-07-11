package lyzzcw.work.component.common.IDUtils;

import org.apache.commons.collections4.map.LRUMap;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/25
 * Time: 10:38
 * Description: 幂等性判断
 *
 * 综合来说：LRUMap 的本质是持有头结点的环回双链表结构，当使用元素时，
 * 就将该元素放在双链表 header 的前一个位置，在新增元素时，如果容量满了就会移除 header 的后一个元素。
 *
 * 仅适用于单机环境下的重复数据拦截，如果是分布式环境需要配合数据库或 Redis 来实现
 *
 * 方案名称	            适用方法	                实现复杂度	        方案缺点
 * 数据库唯一主键	        插入操作 删除操作	        简单	            - 只能用于插入操作；- 只能用于存在唯一主键场景；
 * 数据库乐观锁	        更新操作	                简单	            - 只能用于更新操作；- 表中需要额外添加字段；
 * 请求序列号	            插入操作 更新操作 删除操作	简单	            - 需要保证下游生成唯一序列号；- 需要 Redis 第三方存储已经请求的序列号；
 * 防重 Token 令牌	    插入操作 更新操作 删除操作	适中	            - 需要 Redis 第三方存储生成的 Token 串；
 */
public class IdempotentUtilII {
    // 根据 LRU(Least Recently Used，最近最少使用)算法淘汰数据的 Map 集合，最大容量 100 个
    private static LRUMap<String, Integer> reqCache = new LRUMap<>(100);

    /**
     * 幂等性判断
     * @return
     */
    public static boolean judge(String id, Object lockClass) {
        synchronized (lockClass) {
            // 重复请求判断
            if (reqCache.containsKey(id)) {
                // 重复请求
                System.out.println("请勿重复提交！！！" + id);
                return false;
            }
            // 非重复请求，存储请求 ID
            reqCache.put(id, 1);
        }
        return true;
    }

    public String test(String id) {
        // -------------- 幂等性调用（开始） --------------
        if (!IdempotentUtilII.judge(id, this.getClass())) {
            return "执行失败";
        }
        // -------------- 幂等性调用（结束） --------------
        // 业务代码...
        System.out.println("添加用户ID:" + id);
        return "执行成功！";
    }


}
