package lyzzcw.work.component.common.id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * @author: lzy
 * Date: 2022/5/25
 * Time: 10:38
 * Description: 原子类id生成
 */
@SuppressWarnings("unused")
public class AtomicUtil {


    private static CycleCounter mCounter = new CycleCounter(100000);


    /**
     * 产生每秒有限唯一ID，每秒产生ID不能超过100000个
     *
     * @return 唯一ID
     */
    public static String generateFiniteIDInSecond(){
        return String.format("%d%05d", System.currentTimeMillis()/1000, mCounter.incrementAndGet());
    }




    @SuppressWarnings("WeakerAccess")
    public final static class CycleCounter {
        private final int           max;
        private AtomicInteger count;

        public CycleCounter(int max) {
            if (max < 1) { throw new IllegalArgumentException(); }
            this.max = max;
            this.count = new AtomicInteger();
        }

        public int incrementAndGet() {
            return count.updateAndGet(x -> (x < max - 1) ? x + 1 : 1);
        }
        public int resetAndGet(){
            return count.updateAndGet(x -> 1);
        }
        public void setCount(int pCount){
            count.updateAndGet(x -> pCount);
        }
    }
}
