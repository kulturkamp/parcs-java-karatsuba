import parcs.AM;
import parcs.AMInfo;
import parcs.channel;
import parcs.point;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unchecked")
public class KaratsubaParallel extends Karatsuba implements AM {

    public static int PARALLEL_SIZE_LIMIT = 1 << 16;

    public static channel callKaratsuba(AMInfo info, List<Double> a, List<Double> b) {
        point p = info.createPoint();
        channel c = p.createChannel();
        p.execute("KaratsubaParallel");
        c.write((Serializable) a);
        c.write((Serializable) b);
        return c;
    }

    @Override
    public void run(AMInfo info) {
        List<Double> a = (List<Double>) info.parent.readObject();
        List<Double> b = (List<Double>) info.parent.readObject();
        System.out.println("Multiplication of polynoms started.\n" +
                "A size: " + a.size() + "; B size: " + b.size() + ";");

        List<Double> result;

        if (a.size() > PARALLEL_SIZE_LIMIT) {
            int mid = Math.min(a.size(), b.size()) / 2;

            List<Double> lowA = getPart(a, mid, true);
            List<Double> highA = getPart(a, mid, false);
            List<Double> lowB = getPart(b, mid, true);
            List<Double> highB = getPart(b, mid, false);


            channel z0Channel = callKaratsuba(info, lowA, lowB);
            channel z1Channel = callKaratsuba(info, add(lowA, highA), add(lowB, highB));
            channel z2Channel = callKaratsuba(info, highA, highB);

            List<Double> z0 = (List<Double>) z0Channel.readObject();
            List<Double> z1 = (List<Double>) z1Channel.readObject();
            List<Double> z2 = (List<Double>) z2Channel.readObject();

            result = add(add(shift(z2, 2 * mid), shift(subtract(z1, add(z0, z2)), mid)), z0);
        } else {
            result = multiply(a, b);
        }

        info.parent.write((Serializable) result);
    }
}
