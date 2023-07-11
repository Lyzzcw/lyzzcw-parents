package lyzzcw.work.component.common.random;

import java.util.UUID;

public class SoftRandomUtil {
	private static CustomRandom random = null;

	static {
		random = new CustomRandom(init());
	}

	static long init() {		
		UUID uuid = UUID.randomUUID();
		int hash = uuid.hashCode();
		long time = System.nanoTime();
		return time + hash;
	}

	public static void changeSeed() {
		random.setSeed(init());
	}

	private static void setSeed(long seed) {
		random.setSeed(seed);
	}

	public static long randomLong() {
		return random.nextLong();
	}

	public static long[] randomLongs(int count) {
		long[] nums = new long[count];
		for (int i = 0; i < count; i++) {
			nums[i] = randomLong();
		}
		return nums;
	}

	public static void main(String[] args) {
		setSeed(100L);
		long[] nums = randomLongs(10);
		for (long num : nums)
			System.out.println(num);
	}
}
