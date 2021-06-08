package randomreverser.call.java;

import kaptainwutax.mathutils.util.Mth;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.device.Lattice;

import java.util.function.Predicate;

public class NextInt extends LatticeCall<JRand> {

	private final int bound;
	private final int min;
	private final int max;
	private final boolean bounded;
	private Predicate<Integer> filter;

	protected NextInt(int bound, int min, int max, boolean bounded) {
		this.bound = bound;
		this.min = min;
		this.max = max;
		this.bounded = bounded;
	}

	public NextInt filter(Predicate<Integer> filter) {
		this.filter = filter;
		return this;
	}

	public Predicate<Integer> getFilter() {
		return this.filter;
	}

	public static NextInt withValue(int bound, int value) {
		return inRange(bound, value, value);
	}

	public static NextInt withValue(int value) {
		return inRange(value, value);
	}

	public static NextInt inRange(int bound, int min, int max) {
		return new NextInt(bound, min, max, true);
	}

	public static NextInt inRange(int min, int max) {
		return new NextInt(-1, min, max, false);
	}

	public static SeedCall<JRand> consume(int numSeeds) {
		return Next.consume(LCG.JAVA, numSeeds);
	}

	public static SeedCall<JRand> consume(int bound, int numSeeds) {
		//TODO: add handling for the potential of skipping a call in here
		return Next.consume(LCG.JAVA, numSeeds);
	}

	@Override
	public void build(Lattice<JRand> lattice) {
		if(!this.bounded) {
			lattice.processCall(Next.inBitsRange(32, this.min, this.max + 1));
		} else if((this.bound & -this.bound) == this.bound) {
			int bits = Long.numberOfTrailingZeros(this.bound);
			lattice.processCall(Next.inBitsRange(bits, this.min, this.max + 1));
		} else {
			long m = (1L << 17);
			lattice.processCall(Next.inModRange(this.min * m, (this.max * m) | Mth.getMask(17), this.bound * m));
		}
	}

	@Override
	public boolean test(JRand rand) {
		int value = this.bounded ? rand.nextInt(this.bound) : rand.nextInt();
		if(value < this.min || value > this.max)return false;
		if(this.getFilter() != null && !this.getFilter().test(value))return false;
		return true;
	}

}
