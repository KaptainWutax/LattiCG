package randomreverser.call;

import kaptainwutax.seedutils.rand.Rand;
import randomreverser.device.Lattice;

public abstract class LatticeCall<R extends Rand> {

	public abstract void build(Lattice<R> lattice);

	public abstract boolean test(R rand);

}
