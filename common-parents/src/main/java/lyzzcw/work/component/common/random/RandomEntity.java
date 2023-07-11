package lyzzcw.work.component.common.random;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RandomEntity {
	private BlockingQueue<Long> queue;//随机数使用队列
	private int initAmount;//初始化 个数
	private int addAmount;//追加 个数
	public RandomEntity(int p_initAmount,int p_addAmount,IRandomService p_iGameService) {
		this.queue = new LinkedBlockingQueue<Long>();
		this.initAmount = p_initAmount;
		this.addAmount = p_addAmount;
		getRandomNums(this.initAmount);
	}
	public RandomEntity(int p_initAmount,int p_addAmount) {
		this.queue = new LinkedBlockingQueue<Long>();
		this.initAmount = p_initAmount;
		this.addAmount = p_addAmount;
		getRandomNums(this.initAmount);
	}
	public RandomEntity() {}
	public BlockingQueue<Long> getQueue() {
		return queue;
	}
	public void setQueue(BlockingQueue<Long> queue) {
		this.queue = queue;
	}
	public int getInitAmount() {
		return initAmount;
	}
	public void setInitAmount(int initAmount) {
		this.initAmount = initAmount;
	}
	public int getAddAmount() {
		return addAmount;
	}
	public void setAddAmount(int addAmount) {
		this.addAmount = addAmount;
	}
	// [min,max]
	public int getRandomNum(int min, int max) {
		if (this.queue.isEmpty()) {
			getRandomNums(this.addAmount);
		}
		long originalRandom = this.queue.poll();
		if (originalRandom < 0 || originalRandom > max - min) {
			return (int) (Math.abs(originalRandom % (max - min + 1)) + min);
		}
		return (int) originalRandom + min;
	}
	// [min,max]
	public long getRandomNum(Long min, Long max) {
		if (this.queue.isEmpty()) {
			getRandomNums(this.addAmount);
		}
		long originalRandom = this.queue.poll();
		if (originalRandom < 0 || originalRandom > max - min) {
			return (long) (Math.abs(originalRandom % (max - min + 1)) + min);
		}
		return (long) originalRandom + min;
	}

	public void getRandomNums(int count) {
		// 随机数从交易系统获取
		if(count <= 0) {
			return;
		}
		long[] lnums = SoftRandomUtil.randomLongs(count);
		for(long num:lnums) {
			this.queue.offer(num);
		}
	}
	
	public static void main(String[] args) {
		RandomEntity re  = new RandomEntity(50, 4);
		System.out.println(re.getQueue());
	}
	
}