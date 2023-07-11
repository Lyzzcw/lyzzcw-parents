package lyzzcw.work.component.redis.test;

import cn.hutool.json.JSONUtil;
import lyzzcw.work.component.redis.cache.distribute.DistributeCacheService;
import lyzzcw.work.component.redis.semaphore.DistributedSemaphore;
import lyzzcw.work.component.redis.semaphore.factory.DistributedSemaphoreFactory;
import lyzzcw.work.component.redis.test.bean.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/7/6 9:02
 * Description: No Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DistributeCacheServiceTest {
    @Autowired
    private DistributeCacheService distributeCacheService;

    @Autowired
    private DistributedSemaphoreFactory distributedSemaphoreFactory;
    @Test
    public void testQuery(){
        distributedSemaphoreFactory.getDistributedSemaphore("s").tryAcquire();
    }

    @Test
    public void testQueryWithPassThrough(){
        User user = distributeCacheService.queryWithPassThrough("pass:through:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }
    @Test
    public void testQuerySimpleDataWithPassThrough(){
        Integer id = distributeCacheService.queryWithPassThrough("pass:through2:", 100285210, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }

    @Test
    public void testQueryWithPassThroughList(){
        List<User> list = distributeCacheService.queryWithPassThroughList("pass:through:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQuerySimpleDataWithPassThroughList(){
        List<Integer> list = distributeCacheService.queryWithPassThroughList("pass:through:list2:", 100285211, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithLogicalExpire(){
        User user = distributeCacheService.queryWithLogicalExpire("logical:expire:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }
    @Test
    public void testQuerySimpleDataWithLogicalExpire(){
        Integer id = distributeCacheService.queryWithLogicalExpire("logical:expire2:", 100285212, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }

    @Test
    public void testQueryWithLogicalExpireList(){
        List<User> list = distributeCacheService.queryWithLogicalExpireList("logical:expire:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQuerySimpleDataWithLogicalExpireList(){
        List<Integer> list = distributeCacheService.queryWithLogicalExpireList("logical:expire:list2:", 100285213, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }

    @Test
    public void testQueryWithMutex(){
        User user = distributeCacheService.queryWithMutex("mutex:", 1002852L, User.class,  this::getUser, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(user));
    }

    @Test
    public void testQuerySimpleDataWithMutex(){
        Integer id = distributeCacheService.queryWithMutex("mutex2:", 100285214, Integer.class,  this::getId, 60L, TimeUnit.SECONDS);
        System.out.println(id);
    }
    @Test
    public void testQueryWithMutexList(){
        List<User> list = distributeCacheService.queryWithMutexList("mutex:list:", null, User.class, this::getUserList, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }
    @Test
    public void testQuerySimpleDataWithMutexList(){
        List<Integer> list = distributeCacheService.queryWithMutexList("mutex:list2:", 123, Integer.class, this::getIds, 60L, TimeUnit.SECONDS);
        System.out.println(JSONUtil.toJsonStr(list));
    }


    public User getUser(Long id){
        return new User(id, "binghe");
    }

    public List<User> getUserList(String type){
        return Arrays.asList(
                new User(1L, "binghe001"),
                new User(2L, "binghe002"),
                new User(3L, "binghe003")
        );
    }

    public Integer getId(Integer id){
        return 0;
    }
    public List<Integer> getIds(Integer id){
        return Arrays.asList(0,0,0);
    }
}
