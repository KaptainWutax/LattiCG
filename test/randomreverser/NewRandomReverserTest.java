package randomreverser;

import kaptainwutax.seedutils.lcg.LCG;
import org.junit.Test;
import randomreverser.call.java.*;
import randomreverser.device.JavaRandomDevice;
import randomreverser.device.LCGReverserDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class NewRandomReverserTest {

    @Test
    public void dungeonSeed() {
        String pattern = "111101111111111110011101011110111011011110111101111111101110011";
        int posX = -1699;
        int posY = 38;
        int posZ = -1465;

        JavaRandomDevice device = new JavaRandomDevice();
        device.addCall(NextInt.withValue(16, posX & 15));
        device.addCall(NextInt.withValue(16, posZ & 15));
        device.addCall(NextInt.withValue(256, posY));
        device.addCall(NextInt.consume(2, 2));

        for(char c: pattern.toCharArray()) {
            if(c == '0') {
                device.addCall(NextInt.withValue(4, 0));
            } else if(c == '1') {
                device.addCall(FilteredSkip.filter(LCG.JAVA, r -> r.nextInt(4) != 0, 1));
                //device.addCall(NextInt.inRange(4, 1, 3));
            }
        }

        device.streamSeeds(LCGReverserDevice.Process.EVERYTHING).sequential().limit(1).forEach(System.out::println);
    }

    @Test
    public void azelefSeed() {
        JavaRandomDevice device = new JavaRandomDevice();
        RandomReverser ogDevice = new RandomReverser();

        device.addCall(NextLong.withValue(0L));
        ogDevice.addNextLongCall(0L, 0L);

        List<Long> seeds = ogDevice.findAllValidSeeds().boxed().collect(Collectors.toCollection(ArrayList::new));
        seeds.forEach(System.out::println);
        System.out.println("Finished search with " + seeds.size() + (seeds.size() == 1 ? " seed." : " seeds."));
        System.out.println("The estimate was " + device.getLattice().estimatedSeeds + " seeds.");
        assertEquals(seeds, device.findAllSeeds(LCGReverserDevice.Process.EVERYTHING));
    }

    @Test
    public void eyes14Seeds() {
        JavaRandomDevice device = new JavaRandomDevice();
        RandomReverser ogDevice = new RandomReverser();

        IntStream.range(0, 14).forEach(i -> {
            device.addCall(NextFloat.inRange(0.9F, 1.0F));
            ogDevice.addNextFloatCall(0.9F, 1.0F);
        });

        List<Long> seeds = ogDevice.findAllValidSeeds().boxed().collect(Collectors.toCollection(ArrayList::new));
        seeds.forEach(System.out::println);
        System.out.println("Finished search with " + seeds.size() + (seeds.size() == 1 ? " seed." : " seeds."));
        System.out.println("The estimate was " + device.getLattice().estimatedSeeds + " seeds.");
        assertEquals(seeds, device.findAllSeeds(LCGReverserDevice.Process.EVERYTHING));
    }

    @Test
    public void eyes15SeedDouble() {
        JavaRandomDevice device = new JavaRandomDevice();
        RandomReverser ogDevice = new RandomReverser();

        IntStream.range(0, 15).forEach(i -> {
            device.addCall(NextDouble.inRange(0.9D, 1.0D));
            ogDevice.addNextDoubleCall(0.9D, 1.0D);
        });

        List<Long> seeds = ogDevice.findAllValidSeeds().boxed().collect(Collectors.toCollection(ArrayList::new));
        seeds.forEach(System.out::println);
        System.out.println("Finished search with " + seeds.size() + (seeds.size() == 1 ? " seed." : " seeds."));
        System.out.println("The estimate was " + device.getLattice().estimatedSeeds + " seeds.");
        assertEquals(seeds, device.findAllSeeds(LCGReverserDevice.Process.EVERYTHING));
    }

    @Test
    public void infestedStone11Seed() {
        JavaRandomDevice device = new JavaRandomDevice();
        RandomReverser ogDevice = new RandomReverser();

        IntStream.range(0, 11).forEach(i -> {
            device.addCall(NextFloat.inRange(0.5F, 0.55F));
            ogDevice.addNextFloatCall(0.5F, 0.55F);
        });

        List<Long> seeds = ogDevice.findAllValidSeeds().boxed().collect(Collectors.toCollection(ArrayList::new));
        seeds.forEach(System.out::println);
        System.out.println("Finished search with " + seeds.size() + (seeds.size() == 1 ? " seed." : " seeds."));
        System.out.println("The estimate was " + device.getLattice().estimatedSeeds + " seeds.");
        assertEquals(seeds, device.findAllSeeds(LCGReverserDevice.Process.EVERYTHING));
    }

    @Test
    public void randomStuff() {
        JavaRandomDevice device = new JavaRandomDevice();
        RandomReverser ogDevice = new RandomReverser();

        IntStream.range(0, 4).forEach(i -> {
            device.addCall(NextInt.withValue(1024, 0));
            ogDevice.addNextIntCall(1024, 0, 0);
        });

        List<Long> seeds = ogDevice.findAllValidSeeds().boxed().collect(Collectors.toCollection(ArrayList::new));
        seeds.forEach(System.out::println);
        System.out.println("Finished search with " + seeds.size() + (seeds.size() == 1 ? " seed." : " seeds."));
        System.out.println("The estimate was " + device.getLattice().estimatedSeeds + " seeds.");
        assertEquals(seeds, device.findAllSeeds(LCGReverserDevice.Process.EVERYTHING));
    }

}