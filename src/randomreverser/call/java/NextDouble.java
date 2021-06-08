package randomreverser.call.java;

import kaptainwutax.mathutils.util.Mth;
import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.device.Lattice;

public class NextDouble extends LatticeCall<JRand> {

	private final double min;
	private final double max;
	private final boolean minInclusive;
	private final boolean maxInclusive;

	protected NextDouble(double min, double max, boolean minInclusive, boolean maxInclusive) {
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}

	public static NextDouble withValue(double value) {
		return inRange(value, value);
	}

	public static NextDouble inRange(double min, double max) {
		return new NextDouble(min, max, true, false);
	}

	public static NextDouble inRange(double min, double max, boolean minInclusive, boolean maxInclusive) {
		return new NextDouble(min, max, minInclusive, maxInclusive);
	}

	public static SeedCall<JRand> consume(int numSeeds) {
		return Next.consume(LCG.JAVA, 2 * numSeeds);
	}

	@Override
	public void build(Lattice<JRand> lattice) {
		double minInc = this.min;
		double maxInc = this.max;

		if(!this.minInclusive) {
			minInc = Math.nextUp(this.min);
		}

		if(this.maxInclusive) {
			maxInc = Math.nextUp(this.max);
		}

		long minLong = (long)StrictMath.ceil(minInc * 0x1.0p53);
		long maxLong = (long)StrictMath.ceil(maxInc * 0x1.0p53) - 1;

		if(maxLong < minLong) {
			throw new IllegalArgumentException("call has no valid range");
		}

		lattice.processCall(Next.inBitsRange(26, minLong >> 27, (maxLong >> 27) + 1));

		if(minLong >>> 27 == maxLong >>> 27) { //Can we even say anything about the second half
			lattice.processCall(Next.inBitsRange(27, Mth.mask(minLong, 21), Mth.mask(maxLong, 21)));
		} else {
			lattice.processCall(Next.consume(LCG.JAVA,1));
		}
	}

	@Override
	public boolean test(JRand rand) {
		double value = rand.nextDouble();

		if(this.minInclusive) {
			if(value < this.min)return false;
		} else {
			if(value <= this.min)return false;
		}

		if(this.maxInclusive) {
			return !(value > this.max);
		} else {
			return !(value >= this.max);
		}
	}

}
