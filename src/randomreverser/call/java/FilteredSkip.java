package randomreverser.call.java;

import kaptainwutax.seedutils.lcg.LCG;
import kaptainwutax.seedutils.rand.Rand;
import randomreverser.call.SeedCall;

import java.util.function.Predicate;

public class FilteredSkip<R extends Rand> extends SeedCall.Unmeasured<R> {

	private final Predicate<R> filter;

	protected FilteredSkip(LCG lcg, Predicate<R> filter, int numCalls) {
		super(lcg, numCalls);
		this.filter = filter;
	}

	public static <R extends Rand> FilteredSkip<R> filter(LCG lcg, Predicate<R> filter, int numCalls) {
		return new FilteredSkip<>(lcg, filter, numCalls);
	}

	@Override
	public boolean test(R rand) {
		return this.filter.test(rand);
	}

}
