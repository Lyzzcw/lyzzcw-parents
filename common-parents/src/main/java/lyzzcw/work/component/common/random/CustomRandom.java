
package lyzzcw.work.component.common.random;

import java.util.concurrent.atomic.AtomicLong;

public class CustomRandom {
	private final AtomicLong seed;
	private static final long multiplier = 25214903917L;
	private static final long addend = 11L;
	private static final long mask = 281474976710655L;

	public CustomRandom(long seed) {
		long init = initialScramble(seed);
		this.seed = new AtomicLong(init);
	}

	private static long initialScramble(long seed) {
		return (seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL;
	}

	public void setSeed(long seed) {
		long init = initialScramble(seed);
		this.seed.set(init);
	}

	public long nextLong() {
		long hight = next(32) << 32;
		long low = next(32);
		return hight + low;
	}

	private int next(int bits) {
		long nextseed, oldseed;
		AtomicLong seed = this.seed;

		do {
			oldseed = seed.get();

			nextseed = oldseed * 25214903917L + 11L & 0xFFFFFFFFFFFFL;
		} while (!seed.compareAndSet(oldseed, nextseed));

		return (int) (nextseed >>> 48 - bits);
	}
}
