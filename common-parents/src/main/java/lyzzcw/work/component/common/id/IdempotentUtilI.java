package lyzzcw.work.component.common.id;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Created with IntelliJ IDEA.
 * User: lzy
 * Date: 2022/6/30
 * Time: 16:08
 * Description:  TCC幂等工具类
 */
public class IdempotentUtilI {

    //HashBasedTable 可以用两个key标记一个值
    private static Table<Class<?>,String,Long> map= HashBasedTable.create();

    public static void add(Class<?> clazz,String xid,Long marker){
        map.put(clazz,xid,marker);
    }

    public static Long get(Class<?> clazz,String xid){
        return map.get(clazz,xid);
    }

    public static void remove(Class<?> clazz,String xid){
        map.remove(clazz,xid);
    }



    /**
     *
     * 乐观锁方式之一
     *
     * 1.接收到支付宝支付成功请求
     *
     * 2.查询订单信息
     *
     * select * from t_order where order_id = trade_no;
     * 3.判断订单是已处理
     *
     * 4.如果订单已处理直接返回，若未处理，继续向下执行
     *
     * 5.打开本地事物
     *
     * 6.给本地系统给用户加钱
     *
     * 7.将订单状态置为成功，注意这块是重点，伪代码：
     *
     * update t_order set status = 1 where order_id = trade_no where status = 0;
     * //上面的update操作会返回影响的行数num
     * if(num==1){
     *     //表示更新成功
     *     提交事务;
     * }else{
     *     //表示更新失败
     *     回滚事务;
     * }
     * 关键代码解释：
     *
     * update t_order set status = 1 where order_id = trade_no where status = 0;
     *
     * 这个sql是依靠乐观锁来实现的，status=0作为条件去更新，类似于java中的cas操作。
     *
     * 执行这条sql的时候，如果有多个线程同时到达这条代码，数据内部会保证update同一条记录会排队执行，最终最有一条update会执行成功，此时成功的num为1；其他未成功的，num为0，然后根据num是否为1来判断是否成功 。
     */


    /**
     * 依赖数据库中唯一约束来实现。
     *
     * 创建一个表：
     *
     * CREATE TABLE `t_uq_dipose` (
     *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
     *   `ref_type` varchar(32) NOT NULL DEFAULT '' COMMENT '关联对象类型',
     *   `ref_id` varchar(64) NOT NULL DEFAULT '' COMMENT '关联对象id',
     *   PRIMARY KEY (`id`),
     *   UNIQUE KEY `uq_1` (`ref_type`,`ref_id`) COMMENT '保证业务唯一性'
     * ) ENGINE=InnoDB;
     * 对于任何一个业务，有一个业务类型(ref_type)，业务有一个全局唯一的订单号，业务来的时候，先查询t_uq_dipose表中是否存在相关记录，若不存在，继续放行。
     *
     * 过程如下：
     *
     * 1.接收到支付宝支付成功请求
     *
     * 2.查询t_uq_dipose(条件ref_id,ref_type)，可以判断订单是否已处理
     *
     * select * from t_uq_dipose where ref_type = '充值订单' and ref_id = trade_no;
     * 3.判断订单是已处理
     *
     * 4.如果订单已处理直接返回，若未处理，继续向下执行
     *
     * 5.打开本地事物
     *
     * 6.给本地系统给用户加钱
     *
     * 7.将订单状态置为成功
     *
     * 8.向t_uq_dipose插入数据，插入成功，提交本地事务，插入失败，回滚本地事务，伪代码：
     *
     * try{
     *     insert into t_uq_dipose (ref_type,ref_id) values ('充值订单',trade_no);
     *     提交本地事务：
     * }catch(Exception e){
     *     回滚本地事务;
     * }
     * 关键代码解释：
     *
     * 对于同一个业务，ref_type是一样的，当并发时，插入数据只会有一条成功，其他的会违反唯一约束，进入catch逻辑，当前事务会被回滚，最终最有一个操作会成功，从而保证了幂等性操作。
     *
     * 关于这种方式可以写成通用的方式，不过业务量大的情况下，t_uq_dipose插入数据会成为系统的瓶颈，需要考虑分表操作，解决性能问题。
     *
     * 上面的过程中向t_uq_dipose插入记录，最好放在最后执行，原因：插入操作会锁表，放在最后能让锁表的时间降到最低，提升系统的并发性。
     *
     * 关于消息服务中，消费者如何保证消息处理的幂等性？
     *
     * 每条消息都有一个唯一的消息id，类似于上面业务中的trade_no，使用上面的方式即可实现消息消费的幂等性。
     *
     *  google 工具包里的 Table类可以在JVM中实现类似逻辑
     */
}
