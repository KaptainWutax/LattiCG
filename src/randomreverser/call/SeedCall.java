package randomreverser.call;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.Rand;
import randomreverser.device.Lattice;
import randomreverser.math.component.BigFraction;
import randomreverser.math.component.BigMatrix;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public abstract class SeedCall<R extends Rand> extends LatticeCall<R> {

	public static <R extends Rand> SeedCall.Measured<R> inRange(LCG lcg, long min, long max) {
		return new SeedCall.Measured<>(lcg, min, max);
	}

	public static <R extends Rand> SeedCall.Modulo<R> inModRange(LCG lcg, long min, long max, long mod) {
		return new SeedCall.Modulo<>(lcg, min, max, mod);
	}

	public static <R extends Rand> SeedCall.Unmeasured<R> consume(LCG lcg, long numCalls) {
		return new SeedCall.Unmeasured<>(lcg, numCalls);
	}

	public static class Unmeasured<R extends Rand> extends SeedCall<R> {
		private final LCG lcg;
		private final long numSeeds;

		private final LCG combinedLcg;

		protected Unmeasured(LCG lcg, long numSeeds) {
			this.lcg = lcg;
			this.numSeeds = numSeeds;

			this.combinedLcg = this.lcg.combine(numSeeds);
		}

		@Override
		public void build(Lattice<R> lattice) {
			lattice.currentCallIndex += this.numSeeds;
			lattice.setDirty();
		}

		@Override
		public boolean test(R rand) {
			rand.advance(this.combinedLcg);
			return true;
		}
	}

	public static class Measured<R extends Rand> extends SeedCall<R> {
		private final LCG lcg;
		private final long min;
		private final long max;

		protected Measured(LCG lcg, long min, long max) {
			this.lcg = lcg;
			this.min = min;
			this.max = max;
		}

		@Override
		public void build(Lattice<R> lattice) {
			lattice.minimums.add(this.min);
			lattice.maximums.add(this.max);
			lattice.dimensions += 1;
			lattice.currentCallIndex += 1;
			lattice.callIndices.add(lattice.currentCallIndex);

			BigMatrix newLattice = new BigMatrix(lattice.dimensions + 1, lattice.dimensions);
			if(lattice.dimensions != 1) {
				for(int row = 0; row < lattice.dimensions; row++)
					for(int col = 0; col < lattice.dimensions - 1; col++)
						newLattice.set(row, col, lattice.matrix.get(row, col));
			}

			BigInteger MULT = BigInteger.valueOf(this.lcg.multiplier);
			BigInteger MOD = BigInteger.valueOf(this.lcg.modulus);

			BigInteger tempMult = MULT.modPow(BigInteger.valueOf(lattice.callIndices.get(lattice.dimensions - 1) - lattice.callIndices.get(0)), MOD);
			newLattice.set(0, lattice.dimensions - 1, new BigFraction(tempMult));
			newLattice.set(lattice.dimensions, lattice.dimensions - 1, new BigFraction(MOD));
			lattice.matrix = newLattice;

			lattice.estimatedSeeds = lattice.estimatedSeeds.multiply(BigDecimal.valueOf(max - min + 1)).setScale(10, RoundingMode.HALF_UP);
			lattice.estimatedSeeds = lattice.estimatedSeeds.divide(BigDecimal.valueOf(this.lcg.modulus), RoundingMode.HALF_UP);
			lattice.setDirty();
		}

		@Override
		public boolean test(R rand) {
			long seed = rand.nextSeed();
			return seed >= this.min && seed <= this.max;
		}
	}

	public static class Modulo<R extends Rand> extends SeedCall<R> {
		private final LCG lcg;
		private final long min;
		private final long max;
		private final long mod;

		protected Modulo(LCG lcg, long min, long max, long mod) {
			this.lcg = lcg;
			this.min = min;
			this.max = max;
			this.mod = mod;
		}

		@Override
		public void build(Lattice<R> lattice) {
			long residue = lattice.modulus % this.mod;
			BigInteger MULT = BigInteger.valueOf(this.lcg.multiplier);
			BigInteger MOD = BigInteger.valueOf(this.lcg.modulus);

			if(residue != 0) {
				lattice.failureChance = lattice.failureChance.multiply(BigDecimal.ONE.subtract(
						BigDecimal.valueOf(residue).divide(BigDecimal.valueOf(lattice.modulus), RoundingMode.HALF_UP)));

				//First condition - is the seed real. This conveys more info than it seems since the normal mod vector not present.
				lattice.minimums.add(0L);
				lattice.maximums.add(lattice.modulus - residue); // in the case the seed is > (1L << 48) - residue, the do while in java's nextInt will trigger.
				lattice.currentCallIndex += 1;
				lattice.callIndices.add(lattice.currentCallIndex);
				//Second condition - does the seed have a number within the bounds in its residue class.
				lattice.minimums.add(this.min);
				lattice.maximums.add(this.max);
				lattice.callIndices.add(lattice.currentCallIndex); //We don't increment the call index here because this is really 2 conditions on the same seed

				lattice.dimensions += 2; //We added 2 conditions

				BigMatrix newLattice = new BigMatrix(lattice.dimensions + 1, lattice.dimensions);

				if(lattice.dimensions != 2) { //Copy the old lattice over
					for(int row = 0; row < lattice.dimensions - 1; row++) {
						for(int col = 0; col < lattice.dimensions - 2; col++) {
							newLattice.set(row, col, lattice.matrix.get(row, col));
						}
					}
				}

				BigInteger tempMult = MULT.modPow(BigInteger.valueOf(lattice.callIndices.get(lattice.dimensions - 1) - lattice.callIndices.get(0)), MOD);
				newLattice.set(0, lattice.dimensions - 2, new BigFraction(tempMult));
				newLattice.set(0, lattice.dimensions - 1, new BigFraction(tempMult));

				//vector capturing the effect of the modulo 2^48 operation on the residue class modulo mod
				newLattice.set(lattice.dimensions - 1, lattice.dimensions - 1, new BigFraction(MOD));
				newLattice.set(lattice.dimensions - 1, lattice.dimensions - 2, new BigFraction(MOD));

				//vector identifying everything in residue classes modulo mod
				newLattice.set(lattice.dimensions, lattice.dimensions - 1, new BigFraction(this.mod));

				//update the lattice.
				lattice.matrix = newLattice;
			} else {
				// the conditions are compatible so we can get away with just one new dimension. Caution should
				// be taken in case the this condition is the very first one in the lattice as other calls
				// may force upper bits
				lattice.minimums.add(this.min);
				lattice.maximums.add(this.max);
				lattice.dimensions += 1;
				lattice.currentCallIndex += 1;
				lattice.callIndices.add(lattice.currentCallIndex);
				BigMatrix newLattice = new BigMatrix(lattice.dimensions + 1, lattice.dimensions);

				if(lattice.dimensions != 1) {
					for(int row = 0; row < lattice.dimensions; row++) {
						for(int col = 0; col < lattice.dimensions - 1; col++) {
							newLattice.set(row, col, lattice.matrix.get(row, col));
						}
					}
				} else if(lattice.modulus != this.mod) {
					//TODO find a way to recover the seed in the case we only have constraints of this type
					System.err.println("First call not a bound on a seed. Junk output may be produced.");
				}

				//we might be able to use the provided modulus here instead
				BigInteger tempMult = MULT.modPow(BigInteger.valueOf(lattice.callIndices.get(lattice.dimensions - 1) - lattice.callIndices.get(0)), MOD);
				newLattice.set(0,lattice.dimensions - 1, new BigFraction(tempMult));
				newLattice.set(lattice.dimensions, lattice.dimensions - 1, new BigFraction(this.mod)); //Note this is not MOD.
				lattice.matrix = newLattice;
			}
		}

		@Override
		public boolean test(R rand) {
			long seed = rand.nextSeed() % this.mod;
			return seed >= this.min && seed >= this.max;
		}
	}

}
