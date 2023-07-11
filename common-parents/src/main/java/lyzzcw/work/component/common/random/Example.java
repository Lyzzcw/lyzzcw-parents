package lyzzcw.work.component.common.random;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Example {
	
	private static final Random RANDOM = new Random(System.nanoTime());

	/**
	 * 权重随机数
	 * 
	 * @param weight [15,568,4181,2]
	 * @return 索引值
	 */
	public static int random(List<Integer> weight, RandomEntity randomEntity) {
		List<Integer> weightTmp = new ArrayList<Integer>(weight.size() + 1);
		weightTmp.add(0);
		Integer sum = 0;
		for (Integer d : weight) {
			sum += d;
			weightTmp.add(sum);
		}
		int rand = randomEntity.getRandomNum(0, sum-1);
		int index = 0;
		for (int i = weightTmp.size() - 1; i > 0; i--) {
			if (rand >= weightTmp.get(i)) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 权重随机数
	 * 
	 * @param weight [15,568,4181,2]
	 * @return 索引值
	 */
	public static int randomLong(List<Long> weight, RandomEntity randomEntity) {
		List<Long> weightTmp = new ArrayList<Long>(weight.size() + 1);
		weightTmp.add(0L);
		Long sum = 0L;
		for (Long d : weight) {
			sum += d;
			weightTmp.add(sum);
		}
		long rand = randomEntity.getRandomNum(0L, sum - 1);
		int index = 0;
		for (int i = weightTmp.size() - 1; i > 0; i--) {
			if (rand >= weightTmp.get(i)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	
	/**
	 * 权重随机数
	 * 
	 * @param weight [15,568,4181,2]
	 * @return 索引值
	 */
	public static int random(List<Integer> weight) {
		List<Integer> weightTmp = new ArrayList<Integer>(weight.size() + 1);
		weightTmp.add(0);
		Integer sum = 0;
		for (Integer d : weight) {
			sum += d;
			weightTmp.add(sum);
		}
		int rand = RANDOM.nextInt(sum);
		int index = 0;
		for (int i = weightTmp.size() - 1; i > 0; i--) {
			if (rand >= weightTmp.get(i)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 权重随机数
	 * 
	 * @param weight [15,568,4181,2]
	 * @return 索引值
	 */
	public static int randomLong(List<Long> weight) {
		List<Long> weightTmp = new ArrayList<Long>(weight.size() + 1);
		weightTmp.add(0L);
		Long sum = 0L;
		for (Long d : weight) {
			sum += d;
			weightTmp.add(sum);
		}
		Long rand = nextLong(RANDOM, sum);
		int index = 0;
		for (int i = weightTmp.size() - 1; i > 0; i--) {
			if (rand >= weightTmp.get(i)) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 获取long类型随机数[0,n)
	 * @param rng
	 * @param n
	 * @return
	 */
	public static long nextLong(Random rng,long n) {
		long bits,val;
		do {
			bits = (rng.nextLong() << 1) >>> 1;
			val = bits%n;
		}while(bits-val+(n-1) < 0L);
		return val;
	}
	
	
}
