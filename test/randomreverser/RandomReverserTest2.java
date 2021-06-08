package randomreverser;

import kaptainwutax.seedutils.rand.JRand;
import randomreverser.call.LatticeCall;
import randomreverser.call.java.NextInt;
import randomreverser.device.JavaRandomDevice;
import randomreverser.device.LCGReverserDevice;
import randomreverser.device.Lattice;

public class RandomReverserTest2 {

    public static void main(String[] args) throws Exception {
        /*
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("all_11_eyes.txt")));

        for(int a = 0; a < 11; a++) {
            JavaRandomDevice device = new JavaRandomDevice();

            for(int i = 0; i < 12; i++) {
                if(a != i) {
                    device.addCall(NextFloat.inRange(0.9F, 1.0F));
                } else {
                    device.addCall(NextFloat.consume(1));
                }
            }

            device.streamSeeds(LCGReverserDevice.Process.EVERYTHING).forEach(aLong -> {
                try {
                    writer.write(aLong + "\n");
                    writer.flush();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            });
        }*/

        JavaRandomDevice device = new JavaRandomDevice();
        device.addCall(SquareDecoratorCall.at(8, 3));
        device.addCall(NextInt.withValue(256, 42));

        device.streamSeeds(LCGReverserDevice.Process.EVERYTHING).forEach(seed -> {
            System.out.println(seed);
        });
    }

    public static class SquareDecoratorCall extends LatticeCall<JRand> {
        private final int offsetX;
        private final int offsetZ;

        protected SquareDecoratorCall(int posX, int posZ) {
            this.offsetX = posX & 15;
            this.offsetZ = posZ & 15;
        }

        public static SquareDecoratorCall at(int posX, int posZ) {
            return new SquareDecoratorCall(posX, posZ);
        }

        @Override
        public void build(Lattice<JRand> lattice) {
            lattice.processCall(NextInt.withValue(16, this.offsetX))
                    .processCall(NextInt.withValue(16, this.offsetZ));
        }

        @Override
        public boolean test(JRand rand) {
            if(rand.nextInt(16) != this.offsetX)return false;
            if(rand.nextInt(16) != this.offsetZ)return false;
            return true;
        }
    }

}
