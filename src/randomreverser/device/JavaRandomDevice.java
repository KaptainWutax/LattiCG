package randomreverser.device;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.SeedCall;
import randomreverser.call.java.*;

import java.util.function.Predicate;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class JavaRandomDevice extends LCGReverserDevice<JRand> {

	public JavaRandomDevice() {
		super(LCG.JAVA.modulus);
	}

	@Override
	public Stream<Long> streamSeeds(Process process) {
		if(process != Process.BRUTEFORCE_ONLY) {
			return this.lattice.streamSolutions().filter(seed -> {
				if(process == Process.LATTICE_ONLY)return true;

				JRand rand = new JRand(seed, false);

				for(LatticeCall<JRand> call : this.lattice.getCalls()) {
					if(!call.test(rand)) {
						return false;
					}
				}

				return true;
			});
		}

		return LongStream.range(0L, 1L << 48).filter(seed -> {
			JRand rand = new JRand(seed, false);

			for(LatticeCall<JRand> call : this.lattice.getCalls()) {
				if(!call.test(rand)) {
					return false;
				}
			}

			return true;
		}).boxed();
	}

	public JavaRandomDevice next(long min, long max) {
		this.lattice.addCall(SeedCall.inRange(LCG.JAVA, min, max));
		return this;
	}

	public JavaRandomDevice nextBits(int bits, long min, long max) {
		this.lattice.addCall(Next.inBitsRange(bits, min, max));
		return this;
	}

	public JavaRandomDevice skip(int numSeeds) {
		this.lattice.addCall(Next.consume(numSeeds));
		return this;
	}

	public JavaRandomDevice filterSkip(Predicate<JRand> filter, int numSeeds) {
		this.lattice.addCall(FilteredSkip.filter(LCG.JAVA, filter, numSeeds));
		return this;
	}

	public JavaRandomDevice next(int bits, long min, long max) {
		this.lattice.addCall(Next.inBitsRange(bits, min, max));
		return this;
	}

	public JavaRandomDevice nextBoolean(boolean value) {
		this.lattice.addCall(NextBoolean.withValue(value));
		return this;
	}

	public JavaRandomDevice skipNextBoolean(int numSeeds) {
		this.lattice.addCall(NextBoolean.consume(numSeeds));
		return this;
	}

	public JavaRandomDevice nextInt(int bound, int value) {
		this.lattice.addCall(NextInt.withValue(bound, value));
		return this;
	}

	public JavaRandomDevice nextInt(int bound, int min, int max) {
		this.lattice.addCall(NextInt.inRange(bound, min, max));
		return this;
	}

	public JavaRandomDevice skipNextInt(int bound, int numSeeds) {
		this.lattice.addCall(NextInt.consume(bound, numSeeds));
		return this;
	}

	public JavaRandomDevice nextIntUnbounded(int value) {
		this.lattice.addCall(NextInt.withValue(value));
		return this;
	}

	public JavaRandomDevice nextIntUnbounded(int min, int max) {
		this.lattice.addCall(NextInt.inRange(min, max));
		return this;
	}

	public JavaRandomDevice skipNextIntUnbounded(int numSeeds) {
		this.lattice.addCall(NextInt.consume(numSeeds));
		return this;
	}

	public JavaRandomDevice nextFloat(float value) {
		this.lattice.addCall(NextFloat.withValue(value));
		return this;
	}

	public JavaRandomDevice nextFloat(float min, float max) {
		this.lattice.addCall(NextFloat.inRange(min, max));
		return this;
	}

	public JavaRandomDevice nextFloat(float min, float max, boolean minInclusive, boolean maxInclusive) {
		this.lattice.addCall(NextFloat.inRange(min, max, minInclusive, maxInclusive));
		return this;
	}

	public JavaRandomDevice skipNextFloat(int numSeeds) {
		this.lattice.addCall(NextFloat.consume(numSeeds));
		return this;
	}

	public JavaRandomDevice nextLong(long value) {
		this.lattice.addCall(NextLong.withValue(value));
		return this;
	}

	public JavaRandomDevice nextLong(long min, long max) {
		this.lattice.addCall(NextLong.inRange(min, max));
		return this;
	}

	public JavaRandomDevice skipNextLong(int numSeeds) {
		this.lattice.addCall(NextLong.consume(numSeeds));
		return this;
	}

	public JavaRandomDevice nextDouble(double value) {
		this.lattice.addCall(NextDouble.withValue(value));
		return this;
	}

	public JavaRandomDevice nextDouble(double min, double max) {
		this.lattice.addCall(NextDouble.inRange(min, max));
		return this;
	}

	public JavaRandomDevice nextDouble(double min, double max, boolean minInclusive, boolean maxInclusive) {
		this.lattice.addCall(NextDouble.inRange(min, max, minInclusive, maxInclusive));
		return this;
	}

	public JavaRandomDevice skipNextDouble(int numSeeds) {
		this.lattice.addCall(NextDouble.consume(numSeeds));
		return this;
	}

}
