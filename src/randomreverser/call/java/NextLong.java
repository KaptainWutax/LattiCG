package randomreverser.call.java;

import kaptainwutax.mathutils.util.Mth;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.device.Lattice;

public class NextLong extends LatticeCall<JRand> {

	private final long min;
	private final long max;

	protected NextLong(long min, long max) {
		this.min = min;
		this.max = max;
	}

	public static NextLong withValue(long value) {
		return inRange(value, value);
	}

	public static NextLong inRange(long min, long max) {
		return new NextLong(min, max);
	}

	public static SeedCall<JRand> consume(int numSeeds) {
		return Next.consume(LCG.JAVA, 2 * numSeeds);
	}

	@Override
	public void build(Lattice<JRand> lattice) {
		//TODO warn about / check for the sign bit making a result wrong, 1/4th of results can be false positives in worst case
		boolean minSignBit = (this.min & Mth.getPow2(31)) != 0; //Would a long having value min run into a negative (int) cast
		boolean maxSignBit = (this.max & Mth.getPow2(31)) != 0; //Would a long having value max run into a negative (int) cast

		lattice.processCall(Next.inBitsRange(32, (this.min >>> 32) + (minSignBit ? 1 : 0), (this.max >>> 32) + (maxSignBit ? 2 : 1)));

		if(this.min >>> 32 == this.max >>> 32) { //Can we even talk about the second seed?
			lattice.processCall(Next.inBitsRange(32, Mth.mask(this.min, 32), Mth.mask(this.max, 32) + 1));
		} else {
			lattice.processCall(Next.consume(LCG.JAVA, 1));
		}
	}

	@Override
	public boolean test(JRand rand) {
		long value = rand.nextLong();
		return value >= this.min && value <= this.max;
	}

}
