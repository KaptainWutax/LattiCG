package randomreverser.call.java;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.device.Lattice;

public class NextFloat extends LatticeCall<JRand> {

	private final float min;
	private final float max;
	private final boolean minInclusive;
	private final boolean maxInclusive;

	protected NextFloat(float min, float max, boolean minInclusive, boolean maxInclusive) {
		this.min = min;
		this.max = max;
		this.minInclusive = minInclusive;
		this.maxInclusive = maxInclusive;
	}

	public static NextFloat withValue(float value) {
		return inRange(value, value);
	}

	public static NextFloat inRange(float min, float max) {
		return inRange(min, max, true, false);
	}

	public static NextFloat inRange(float min, float max, boolean minInclusive, boolean maxInclusive) {
		return new NextFloat(min, max, minInclusive, maxInclusive);
	}

	public static SeedCall<JRand> consume(int numSeeds) {
		return Next.consume(LCG.JAVA, numSeeds);
	}

	@Override
	public void build(Lattice<JRand> lattice) {
		float minInc = this.min;
		float maxInc = this.max;

		if(!this.minInclusive) {
			minInc = Math.nextUp(this.min);
		}

		if(this.maxInclusive) {
			maxInc = Math.nextUp(this.max);
		}

		// inclusive
		long minLong = (long)StrictMath.ceil(minInc * 0x1.0p24f);
		long maxLong = (long)StrictMath.ceil(maxInc * 0x1.0p24f) - 1;

		if(maxLong < minLong) {
			throw new IllegalArgumentException("call has no valid range");
		}

		lattice.processCall(Next.inBitsRange(24, minLong, maxLong + 1));
	}

	@Override
	public boolean test(JRand rand) {
		float value = rand.nextFloat();

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
