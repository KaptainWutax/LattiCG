package randomreverser.device;

import kaptainwutax.seedutils.rand.Rand;
import randomreverser.call.LatticeCall;

import java.util.stream.LongStream;

public abstract class LCGReverserDevice<R extends Rand> {

	protected Lattice<R> lattice;

	public LCGReverserDevice(long modulus) {
		this.lattice = new Lattice<>(modulus);
	}

	public Lattice<R> getLattice() {
		return this.lattice;
	}

	public void setLattice(Lattice<R> lattice) {
		this.lattice = lattice;
	}

	public void addCall(LatticeCall<R> call) {
		this.lattice.addCall(call);
	}

	public void processCall(LatticeCall<R> call) {
		this.lattice.processCall(call);
	}

	public long[] findAllSeeds(Process process) {
		return this.streamSeeds(process).toArray();
	}

	public abstract LongStream streamSeeds(Process process);

	public enum Process {
		LATTICE_ONLY, BRUTEFORCE_ONLY, EVERYTHING
	}
}
