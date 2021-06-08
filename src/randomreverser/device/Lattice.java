package randomreverser.device;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.Rand;
import randomreverser.call.LatticeCall;
import randomreverser.math.component.BigFraction;
import randomreverser.math.component.BigMatrix;
import randomreverser.math.component.BigVector;
import randomreverser.math.lattice.LLL.LLL;
import randomreverser.math.lattice.LLL.Params;
import randomreverser.math.lattice.LLL.Result;
import randomreverser.math.lattice.enumeration.Enumerate;
import randomreverser.util.Mth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

import static randomreverser.math.lattice.LLL.Params.recommendedDelta;

public class Lattice<R extends Rand> {

	public long modulus;

	public List<Long> minimums = new ArrayList<>();
	public List<Long> maximums = new ArrayList<>();
	public List<Long> callIndices = new ArrayList<>();
	public long currentCallIndex = 0;
	public int dimensions = 0;

	private List<LatticeCall<R>> calls = new ArrayList<>();

	public BigDecimal estimatedSeeds;
	public BigDecimal failureChance;

	private boolean dirty = true;
	public BigMatrix matrix;
	public BigMatrix reducedMatrix;

	public Lattice(long modulus) {
		this.modulus = modulus;
		this.estimatedSeeds = BigDecimal.valueOf(this.modulus).setScale(10, RoundingMode.HALF_UP);
		this.failureChance = BigDecimal.ZERO;
	}

	public BigMatrix getMatrix() {
		return this.matrix;
	}

	public List<LatticeCall<R>> getCalls() {
		return this.calls;
	}

	public void setDirty() {
		this.dirty = true;
	}

	public Lattice<R> processCall(LatticeCall<R> call) {
		call.build(this);
		return this;
	}

	public Lattice<R> addCall(LatticeCall<R> call) {
		this.calls.add(call);
		return this.processCall(call);
	}

	public void build() {
		if(!this.dirty)return;

		//The lengths of the sides of the cuboid in which our seeds must fall
		BigInteger[] sideLengths =  new BigInteger[this.dimensions];

		for(int i = 0; i < this.dimensions; i++) {
			sideLengths[i] = BigInteger.valueOf(this.maximums.get(i) - this.minimums.get(i) + 1);
		}

		BigInteger lcm = BigInteger.ONE;

		for(int i = 0; i < this.dimensions; i++) {
			lcm = Mth.lcm(lcm, sideLengths[i]);
		}

		BigMatrix scales = new BigMatrix(this.dimensions, this.dimensions);

		for(int i = 0; i < this.dimensions; i++) {
			for(int j = 0; j < this.dimensions; j++) {
				scales.set(i, j, BigFraction.ZERO);
			}

			scales.set(i, i, new BigFraction(lcm.divide(sideLengths[i])));
		}

		BigMatrix unscaledLattice = this.matrix;

		BigMatrix scaledLattice = unscaledLattice.multiply(scales);
		Params params = new Params().setDelta(recommendedDelta).setDebug(false);

		Result result = LLL.reduce(scaledLattice, params);
		this.reducedMatrix = result.getReducedBasis().multiply(scales.inverse());
		this.dirty = false;
	}

	public LongStream streamSolutions() {
		this.build();

		BigVector lower = new BigVector(this.dimensions);
		BigVector upper = new BigVector(this.dimensions);
		BigVector offset = new BigVector(this.dimensions);
		long seed = 0L;

		for(int i = 0; i < this.dimensions; i++) {
			lower.set(i, new BigFraction(this.minimums.get(i)));
			upper.set(i, new BigFraction(this.maximums.get(i)));
			offset.set(i, new BigFraction(seed));

			if(i != this.dimensions - 1) {
				//TODO: support arbitrary LCGs
				seed = LCG.JAVA.combine(this.callIndices.get(i + 1) - this.callIndices.get(i)).nextSeed(seed);
			}
		}

		//TODO: support arbitrary LCGs
		LCG back = LCG.JAVA.combine(-this.callIndices.get(0));

		return Enumerate.enumerate(this.reducedMatrix.transpose(), lower, upper, offset)
			.map(vec -> vec.get(0))
			.map(BigFraction::getNumerator)
			.mapToLong(BigInteger::longValue)
			.map(back::nextSeed);
	}

	public Lattice<R> copy() {
		Lattice<R> copy = new Lattice<>(this.modulus);
		copy.minimums = new ArrayList<>(this.minimums);
		copy.maximums = new ArrayList<>(this.maximums);
		copy.callIndices = new ArrayList<>(this.callIndices);
		copy.currentCallIndex = this.currentCallIndex;
		copy.dimensions = this.dimensions;
		copy.calls = new ArrayList<>(this.calls);
		copy.estimatedSeeds = this.estimatedSeeds;
		copy.matrix = this.matrix != null ? this.matrix.copy() : null;
		copy.reducedMatrix = this.reducedMatrix != null ? this.reducedMatrix.copy() : null;
		return copy;
	}

}
