package randomreverser.call.java;

import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.device.Lattice;

public class NextBoolean extends LatticeCall<JRand> {

	private final boolean value;

	protected NextBoolean(boolean value) {
		this.value = value;
	}

	public static NextBoolean withValue(boolean value) {
		return new NextBoolean(value);
	}

	public static SeedCall<JRand> consume(int numSeeds) {
		return NextInt.consume(2, numSeeds);
	}

	@Override
	public void build(Lattice<JRand> lattice) {
		lattice.processCall(NextInt.withValue(2, this.value ? 1 : 0));
	}

	@Override
	public boolean test(JRand rand) {
		return rand.nextBoolean() == this.value;
	}

}
