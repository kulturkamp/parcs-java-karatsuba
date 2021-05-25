import java.util.ArrayList;
import java.util.List;

public class Karatsuba {

    public static List<Double> add(List<Double> a, List<Double> b) {
        List<Double> c = new ArrayList<>();
        for (int i = 0; i < Math.max(a.size(), b.size()); i++) {
            c.add((i < a.size() ? a.get(i) : 0) + (i < b.size() ? b.get(i) : 0));
        }
        return c;
    }

    public static List<Double> subtract(List<Double> a, List<Double> b) {
        List<Double> c = new ArrayList<>();
        for (int i = 0; i < Math.max(a.size(), b.size()); i++) {
            c.add((i < a.size() ? a.get(i) : 0) - (i < b.size() ? b.get(i) : 0));
        }
        return c;
    }

    public static List<Double> shift(List<Double> a, int k) {
        List<Double> c = new ArrayList<>();
        for (int i = 0; i < k; i++) c.add(0.0);
        c.addAll(a);
        return c;
    }

    public static List<Double> naive(List<Double> a, List<Double> b) {
        List<Double> c = new ArrayList<>();
        for (int i = 0; i < a.size() + b.size() - 1; i++) {
            c.add(0.0);
        }
        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < b.size(); j++) {
                c.set(i + j, c.get(i + j) + a.get(i) * b.get(j));
            }
        }
        return c;
    }

    public static List<Double> multiply(List<Double> a, List<Double> b) {
        if (a.size() <= 32 || b.size() <= 32) {
            return naive(a, b);
        }
        int mid = Math.min(a.size(), b.size()) / 2;
        List<Double> lowA = getPart(a, mid, true);
        List<Double> highA = getPart(a, mid, false);
        List<Double> lowB = getPart(b, mid, true);
        List<Double> highB = getPart(b, mid, false);

        List<Double> z0 = multiply(lowA, lowB);
        List<Double> z1 = multiply(add(lowA, highA), add(lowB, highB));
        List<Double> z2 = multiply(highA, highB);

        return add(add(shift(z2, 2 * mid), shift(subtract(z1, add(z0, z2)), mid)), z0);
    }

    public static List<Double> getPart(List<Double> a, int mid, boolean first_part) {
        List<Double> part = new ArrayList<>();
        if (first_part) {
            for (int i = 0; i < mid; i++) {
                part.add(a.get(i));
            }
        } else {
            for (int i = mid; i < a.size(); i++) {
                part.add(a.get(i));
            }
        }
        return part;
    }

}
