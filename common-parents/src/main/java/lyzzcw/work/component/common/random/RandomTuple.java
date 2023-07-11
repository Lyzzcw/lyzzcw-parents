package lyzzcw.work.component.common.random;

import java.io.Serializable;
import java.util.Arrays;

public class RandomTuple implements Serializable {
	private long[] randomNos;
	private String sign;
	@Deprecated
	private int[] numbers;

	public RandomTuple(int[] numbers, String sign) {
		this.numbers = numbers;
		this.sign = sign;
	}

	public RandomTuple(long[] randomNos, String sign) {
		this.randomNos = randomNos;
		this.sign = sign;
	}

	public long[] getRandomNos() {
		return this.randomNos;
	}

	public void setRandomNos(long[] randomNos) {
		this.randomNos = randomNos;
	}

	public String getSign() {
		return this.sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int[] getNumbers() {
		return this.numbers;
	}

	public void setNumbers(int[] numbers) {
		this.numbers = numbers;
	}

	public String toString() {
		return "RandomTuple{numbers=" + Arrays.toString(this.numbers) + ", sign='" + this.sign + '\'' + '}';
	}
}
