import parcs.AMInfo;
import parcs.channel;
import parcs.point;
import parcs.task;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        task curtask = new task();
        curtask.addJarFile("KaratsubaParallel.jar");

        AMInfo info = new AMInfo(curtask, null);
        Scanner sc = new Scanner(new File(curtask.findFile("input.txt")));
        
        int n = sc.nextInt();
        int m = sc.nextInt();

        List<Double> a = new ArrayList<>();
        List<Double> b = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            a.add(Math.random() - 0.5);
        }
        for (int i = 0; i < m; i++) {
            b.add(Math.random() - 0.5);
        }

        point p = info.createPoint();
        channel c = p.createChannel();

        p.execute("KaratsubaParallel");

        c.write((Serializable) a);
        c.write((Serializable) b);

        List<Double> res = (List<Double>) c.readObject();

        System.out.println("Result polynomial coefficients: ");

        for (int i = 0; i < Math.min(16, res.size()); i++) {
            System.out.print(res.get(i));
            System.out.print(", ");
        }
        if (res.size() > 16)
            System.out.print("...");
        System.out.println();

        curtask.end();
    }

}
