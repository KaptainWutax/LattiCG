package randomreverser.call.java;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import kaptainwutax.seedutils.rand.Rand;
import randomreverser.call.SeedCall;

public class Next extends SeedCall.Measured<JRand> {

	protected Next(LCG lcg, long min, long max) {
		super(lcg, min, max);
	}

	public static <R extends Rand> SeedCall.Unmeasured<R> consume(long numCalls) {
		return SeedCall.consume(LCG.JAVA, numCalls);
	}

	public static Next inBitsRange(int bits, long min, long max) {
		return new Next(LCG.JAVA, min << (48 - bits), (max << (48 - bits)) - 1);
	}

	public static <R extends Rand> SeedCall.Modulo<R> inModRange(long min, long max, long mod) {
		return SeedCall.inModRange(LCG.JAVA, min, max, mod);
	}

}
