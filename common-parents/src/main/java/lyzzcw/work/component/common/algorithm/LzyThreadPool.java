package lyzzcw.work.component.common.algorithm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lzy
 * @version 1.0
 * Date: 2023/8/29 11:30
 * Description: 自定义一个线程池，你的请求是要包装成任务传进线程池的
 */
public class LzyThreadPool {
    public static void main(String[] args) {
        // 没有拒绝策略的实现
        /*ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS,
        10,null);
        for (int i = 0; i < 25; i++) {
        int j = i;
        threadPool.execute(() -> {
        try {
        // 多睡会，模拟任务满了，线程数不够了，不然你瞬间执行完回来就能执行后面
        的，一直满不了
        Thread.sleep(1000000L);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        log.debug("{}", j);
        });
        }*/
        // 拒绝策略传入函数式编程实现即可，不想多写实现类了
        ThreadPool threadPool = new ThreadPool(1,
                1000, TimeUnit.MILLISECONDS, 1, (queue, task) -> {
            // 1. 死等策略
            // queue.put(task);
            // 2) 带超时等待策略
            // queue.offer(task, 1500, TimeUnit.MILLISECONDS);
            // 3) 让调用者放弃任务执行
            // log.debug("放弃{}", task);
            // 4) 让调用者抛出异常，剩余的任务也就不执行了，就是主线程抛出异常，也就是主线程自己去抛出异常，因为没有交给workers里面的线程去做，
            // 这是在主线程调用的，没传到线程池里
            // throw new RuntimeException("任务执行失败 " + task);
            // 5) 让调用者自己执行任务策略，也就是主线程自己去run，因为没有交给workers里面的线程去做，这是在主线程调用的，没传到线程池里
            task.run();
        });
    }
}

// 实现线程池的阻塞队列
@Slf4j
class BlockingQueue<T> {
    /**
     * 1、线程池得有一个阻塞队列，不然不能一直往进放任务，超过的就阻塞，一般用双向列
     * 表.ArrayDeque性能比linked要好点
     */
    private Deque<T> queue = new ArrayDeque<>();
    /**
     * 2、需要一个锁，因为你队列里可能有多个线程去头部取任务，只能有一个线程拿到，要是都拿到可
     * 能就操作多次，重复执行了
     * 同样添加也是只能加一个，别重了，用重入锁更灵活
     */
    private ReentrantLock lock = new ReentrantLock();
    /**
     * 需要两个条件变量。一个是取任务的消费者，你不能线程一直在那取，耗性能。所以空的时候就等
     * 待。一个是生产任务的，一般
     * 是我们的客户端。你也不能一直往进放，满了就等待吧。配合ReentrantLock所以用条件变量
     */
    // 3、生产者条件变量，就是满的时候触发条件去阻塞，所以是full
    Condition fullWaitSet = lock.newCondition();
    // 4、消费者条件变量，就是空的时候触发条件去阻塞，所以是empty
    Condition emptyWaitSet = lock.newCondition();
    // 5. 队列容量，创建的时候指定能放几个任务进去
    private int capcity;

    public BlockingQueue(int capcity) {
        this.capcity = capcity;// 构造函数
    }

    // 带超时阻塞获取，不能一直等，所以提供这个方法，比较灵活
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // 将 timeout 统一转换为 纳秒，统一时间单位处理
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()) {
                try {
                    // 剩余时间小于0就是你没等到，超时了你还在这里，那就结束吧，累了
                    if (nanos <= 0) {
                        return null;
                    }
                            // awaitNanos是你的剩余时间，避免之前说的虚假唤醒。出现假如你没等够就被唤醒了，那你再回来走一次while又是一个timeout时间
                            // 所以这里做了处理，你再回来就是timeout - 经过的时间，重新赋值给nanos最终保证你就等了timeout时间。因为我们有多个线程所以
                            // 防止虚假唤醒
                            nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞获取
    public T take() {
        // 获取任务需要加锁，避免重复获取
        lock.lock();
        try {
            // 判断队列是不是空的
            while (queue.isEmpty()) {
                try {
                    // 要是空的就一直在这等，等生产者别人唤醒,唤醒之后再出去判断一次while，不空就下去执行
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 非空就到这里了，取出头部第一个，并且移除，返回
            T t = queue.removeFirst();
            // 唤醒添加队列的锁条件
            fullWaitSet.signal();
            return t;
        } finally {
            // 最终释放锁
            lock.unlock();
        }
    }

    // 往任务队列里添加任务，阻塞添加
    public void put(T task) {
        // 加锁避免重复添加
        lock.lock();
        try {
            // 如果队列任务满了，就进去等待，等消费者唤醒,唤醒之后再出去判断一次while，不满就下去执行
            while (queue.size() == capcity) {
                try {
                    log.debug("任务队列已满，等待加入任务队列 {} ...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            // 在队列尾部添加一个任务
            queue.addLast(task);
            // 唤醒获取队列的锁条件
            emptyWaitSet.signal();
        } finally {
            // 最终释放锁
            lock.unlock();
        }
    }

    // 带超时时间阻塞添加，超时了直接返回false
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);
            while (queue.size() == capcity) {
                try {
                    if (nanos <= 0) {
                        return false;
                    }
                    log.debug("等待加入任务队列 {} ...", task);
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    // 有拒绝策略的put
    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            // 判断队列是否满
            if (queue.size() == capcity) {
                // 满了就走拒绝策略
                rejectPolicy.reject(this, task);
            } else { // 有空闲
                log.debug("加入任务队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    // 获取队列的当前大小吧
    public int size() {
        // 避免当时有添加的，所以先锁住
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

// 实现线程池
@Slf4j
class ThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;
    // 线程集合,我们包装一层，不直接暴露thread这种，变成我们自己的worker，消息队列都这么叫
    private HashSet<Worker> workers = new HashSet<>();
    // 核心线程数
    private int coreSize;
    // 获取任务时的超时时间，超过这个时间还没任务执行，就让线程结束，避免创建出来不用浪费资源
    private long timeout;
    // 时间单位
    private TimeUnit timeUnit;
    private RejectPolicy<Runnable> rejectPolicy;

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数没有超过 coreSize 时，直接交给 worker 对象执行
        // 如果任务数超过 coreSize 时，加入任务队列暂存
        synchronized (workers) {// HashSet不是线程安全，所以加个锁吧
            if (workers.size() < coreSize) {
                // 没超过，直接创建一个线程对象，加入线程集合，后面取出来去执行任务
                Worker worker = new Worker(task);
                log.debug("新增 worker{}, {}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
                // 如果超过了核心数，就把任务加进队列，等那边线程忙完了取出来操作
                // 1) 死等，调用阻塞put
                // taskQueue.put(task);
                // 2) 带超时等待
                // 3) 让调用者放弃任务执行
                // 4) 让调用者抛出异常
                // 5) 让调用者自己执行任务，就不让线程池执行了
                taskQueue.tryPut(rejectPolicy, task);// 传入拒绝策略，让具体策略处理tryPut
            }
        }
    }

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int
            queueCapcity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.rejectPolicy = rejectPolicy;
    }

    // 包装一层我们自己的线程对象
    class Worker extends Thread {
        // Runnable任务，执行任务，外面传进来
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1) 当 task 不为空，执行任务
            // 2) 当 task 执行完毕，再接着从任务队列获取任务并执行
            //while(task != null || (task = taskQueue.take()) != null) {// 不断去取，去处理
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) !=
                    null) {//超时了就结束就不在这等了
                try {
                    log.debug("正在执行...{}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }
            // 移除是要加锁的，避免多个线程都去移除这个任务
            synchronized (workers) {
                log.debug("worker 被移除{}", this);
                // 执行完了就移除这个线程，没必要在了，我们没设置常驻线程
                workers.remove(this);
            }
        }
    }
}

/**
 * @description:拒绝策略，给线程池的使用这策略模式，抽象出一个接口，策略具体实现各种策略，具体 执行交给调用者，
 * 也就是调用线程池执行任务的线程比如主线程
 * 拒绝策略指的是，任务队列满了，还有任务进来，这时候调用者到底用哪个策略处理
 * @author:LWQ
 * @version:1.0
 */
@FunctionalInterface // 拒绝策略，就一个方法，定义成函数式接口
interface RejectPolicy<T> {
    /**
     * @param queue 你的哪个队列，满了需要让调用者知道，看看他怎么处理这个队列，是移除一个任
     *              务呢还是啥，反正就这两个角色
     * @param task  这个加进来的任务也需要让调用者知道，看看他怎么处理这个任务
     */
    void reject(BlockingQueue<T> queue, T task);
}

